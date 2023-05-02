package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.StationsRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.StopRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.TicketRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The interface StopService com.example.epam.finalProject.Railwayticketoffice.services.
 * @author Ivan Volchenko
 */
@Service
public class StopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopService.class);
    private final StopRepository stopRepository;

    private final StationsRepository stationsRepository;
    private final TicketRepository ticketRepository;

    public StopService(StopRepository stopRepository, StationsRepository stationsRepository,
                       TicketRepository ticketRepository) {
        this.stopRepository = stopRepository;
        this.stationsRepository = stationsRepository;
        this.ticketRepository=ticketRepository;
    }

    public boolean createNewRoute (Stop first, Stop second, long departure, long arrival){
        LOGGER.info("StopService: method 'createNewRoute'");
        Optional<Station> stationFirst = stationsRepository.findById(departure);
        Optional<Station> stationSecond = stationsRepository.findById(arrival);
        if (stationFirst.isEmpty() || stationSecond.isEmpty()) return false;
        if (!differenceBetweenDatesForCreating(first.getDeparture(),second.getDeparture())) return false;
        List <Stop> checkStop = stopRepository.findAllByTrain(first.getTrain());
        if (!checkStop.isEmpty()) return false;
        Station stationStart = stationFirst.get();
        first.setStation(stationStart);
        Station stationFinish = stationSecond.get();
        second.setStation(stationFinish);
        stopRepository.save(first);
        stopRepository.save(second);
        return true;
    }

    public boolean differenceBetweenDatesForCreating (LocalDateTime first, LocalDateTime second){
        if (first.isBefore(LocalDateTime.now().plusMinutes(1))){
            return false;
        }
        return second.isAfter(first);
    }

    public boolean deleteStop(long st) {
        LOGGER.info("StopService: method 'delete'");
        Optional<Stop> stopFound = stopRepository.findById(st);
        AtomicBoolean result = new AtomicBoolean(false);
        if (stopFound.isPresent()) {
            Stop stop = stopFound.get();
            result.set(true);
            stopRepository.deleteById(stop.getId());
        }
        return result.get();
    }

    public boolean addStop(long station, String number, int km,
                           LocalDateTime in, LocalDateTime out) {
        LOGGER.info("StopService: method 'addStop'");
        if (out.isBefore(in)) return false;
        Optional<Station> byId = stationsRepository.findById(station);
        if (byId.isEmpty()) return false;
        List<Stop> byTrain = stopRepository.findByTrain(number);
        if (byTrain.isEmpty()) return false;
        AtomicBoolean result = new AtomicBoolean(false);
                byTrain.forEach(e->{
                    if (e.getStation().getId()==station){
                        result.set(true);
                    }
                });
                if (result.get()) return false;
        Stop stop = new Stop(number,in,out,km);
        stop.setStation(byId.get());
        stopRepository.save(stop) ;
        return true;
    }

    /**
     * It changes information about stops
     * @param id the stop identifier
     * @param km The distance from the start of the route
     * @param timein The arrival time
     * @param timeout The departure time
     * @return the boolean
     */
    public boolean change(long id,int km,LocalDateTime timein,LocalDateTime timeout) {
        LOGGER.info("StopService: method 'change'");
        if (timein.isBefore(LocalDateTime.now().plusMinutes(1))) return false;
        if (timeout.isBefore(timein)) return false;
        Optional<Stop> stopFound = stopRepository.findById(id);
        AtomicBoolean result = new AtomicBoolean(false);
                if (stopFound.isPresent()){
                    Stop stop = stopFound.get();
                    result.set(true);
                    stop.setKm(km);
                    stop.setArrival(timein);
                    stop.setDeparture(timeout);
                    stopRepository.save(stop);
                }
        return result.get();
    }

    /**
     * It returns all routes between the given stations information about stops ( is called from main page)
     * @param from the departure station
     * @param to the arrival station
     * @return the List of Rotes
     */
    public ArrayList <Route> search(String from, String to) {
        LOGGER.info("StopService: method 'search'");
        ArrayList<Station> allByCity = stationsRepository.findAllByCity(from);
        if (allByCity.isEmpty()) return new ArrayList <Route> ();
        ArrayList<Station> allByCity2 = stationsRepository.findAllByCity(to);
        if (allByCity2.isEmpty()) return new ArrayList <Route> ();
        ArrayList <Route> routes = new ArrayList<>();
        for (Station firstStation:allByCity){
            List<Stop> allByStation_id = stopRepository.findAllByStation_id(firstStation.getId());
            for (Station secondStation: allByCity2){
                List<Stop> allByStation_id2 = stopRepository.findAllByStation_id(secondStation.getId());
                allByStation_id2.forEach(stop -> {
                    for (Stop list: allByStation_id){
                        if (list.getTrain().equals(stop.getTrain())){
                            if (list.getKm()<stop.getKm()){
                                String time = calculateTime(list, stop);
                                double value = (stop.getKm()-list.getKm())*list.getPrice();
                                String valueFormer = String.valueOf(value).substring(0,2);
                                value = Double.parseDouble(valueFormer);
                                int seats = list.getSeats();
                                String in = stop.getArrival().toString().replace('T',' ');
                                String out = list.getDeparture().toString().replace('T',' ');
                                routes.add(new Route(list.getTrain(),in,list.getStation().getCity() + ", "
                                        + list.getStation().getStreet(), time  ,out,
                                        stop.getStation().getCity() + ", "+ stop.getStation().getStreet(),
                                        value,seats,list.getId(),stop.getId()));
                            }}}});
            }
        }
        return routes;
    }

    public String calculateTime (Stop first, Stop second){
        LOGGER.info("StopService: method 'calculateTime'");
        long result = ChronoUnit.MINUTES.between(first.getDeparture(), second.getArrival());
        long hours = result/60;
        long minutes = (result-hours*60);
        if (hours>0){
            return hours + " h " + minutes + " m";
        }
        return minutes + " m";

    }

    /**
     * It returns all routes between the given stations information about stops
     * @param id the departure stop identifier
     * @param secondId the arrival stop identifier
     * @return the List of Rotes
     */
    public List<Route> check(long id,long secondId) {
        LOGGER.info("StopService: method 'check'");
        Optional<Stop> byId = stopRepository.findById(id);
        Stop stop = byId.get();
        String city = stop.getStation().getCity();
        List<Stop> byTrain = stopRepository.findByTrain(stop.getTrain());
        List<Stop> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(
                "[а-яА-ЯёЁ\\s]*");
        Matcher matcher = pattern.matcher(city);
        if (matcher.matches()){
            byTrain.forEach(el->{
                Matcher matcherCount = pattern.matcher(el.getStation().getCity());
                if (matcherCount.matches()){
                    result.add(el);
                }
            });
        } else {
            byTrain.forEach(el->{
                Matcher matcherCount = pattern.matcher(el.getStation().getCity());
                if (!matcherCount.matches()){
                    result.add(el);
                }
            });
        }
        List<Route> stops = new ArrayList<>();
        result.forEach(el->{
            String ar = el.getArrival().toString().replace('T',' ');
            String dep = el.getDeparture().toString().replace('T',' ');
            Route route = new Route(el.getTrain(),ar,el.getStation().getCity() + " , " + el.getStation().getStreet(),
                    dep,el.getKm(),el.getId(),el.getId());
            stops.add(route);
        });
        return stops;
    }


    public void buyTicket(User user, List<Stop> allOfStops, long id, long secondId) {
        LOGGER.info("StopService: method 'buyTicket'");
        int seat = buyTicketSaveStops(allOfStops);
        List<Route> stops = check(id, secondId);
        Ticket ticket = new Ticket();
        ticket.setUuid(UUID.randomUUID().toString());
        ticket.setUser(user);
        ticket.setTran(allOfStops.get(1).getTrain());
        ticket.setSeat(seat);
        for (Stop stop: allOfStops){
            if (stop.getId()==id) {
                ticket.setStopDeparture(stop);
            }
            if (stop.getId()==secondId){
                ticket.setStopArrival(stop);
            }
        }
        ticketRepository.save(ticket);
    }

    public int buyTicketSaveStops (List<Stop> allOfStops){
        int seat=0;
        for (Stop stop:allOfStops){
            seat=stop.getSeats();
            int b = stop.getSeats() -1;
            stop.setSeats(b);
        }
        stopRepository.saveAll(allOfStops);
        return seat;
    }

    public Page<Stop> findAllStops(int page, int size) {
        LOGGER.info("StopService: method 'findAllStops'");
        Pageable pageable = PageRequest.of(page - 1,size);
        return stopRepository.findAll(pageable);
    }
}
