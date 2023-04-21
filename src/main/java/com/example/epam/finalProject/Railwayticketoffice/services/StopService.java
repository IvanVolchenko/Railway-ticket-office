package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.StationsRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.StopRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Route;
import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import com.example.epam.finalProject.Railwayticketoffice.models.Stop;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StopService {

    private StopRepository stopRepository;

    private StationsRepository stationsRepository;

    public StopService(StopRepository stopRepository, StationsRepository stationsRepository) {
        this.stopRepository = stopRepository;
        this.stationsRepository = stationsRepository;
    }

    public boolean createNewRoute (Stop first, Stop second, long departure, long arrival){
        Optional<Station> stationFirst = stationsRepository.findById(departure);
        Optional<Station> stationSecond = stationsRepository.findById(arrival);
        if (stationFirst.isEmpty() || stationSecond.isEmpty()) {
            return false;
        }
        if (!differenceBetweenDatesForCreating(first.getDeparture(),second.getDeparture())){
            return false;
        }
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

    public boolean delete(long station, String number) {
        List<Stop> byTrain = stopRepository.findByTrain(number);
        AtomicBoolean result = new AtomicBoolean(false);
        if (byTrain.size()>0){
            byTrain.forEach(e->{
                if (e.getStation().getId()==station){
                    result.set(true);
                    stopRepository.deleteById(e.getId());
                }
            });
        }
        return result.get();
    }

    public void addStop(long station, String number, int km,
                           LocalDateTime in, LocalDateTime out) {
        if (out.isBefore(in)) return;
        Optional<Station> byId = stationsRepository.findById(station);
        if (byId.isEmpty()) return;
        List<Stop> byTrain = stopRepository.findByTrain(number);
        if (byTrain.isEmpty()) return;
        AtomicBoolean result = new AtomicBoolean(false);
                byTrain.forEach(e->{
                    if (e.getStation().getId()==station){
                        result.set(true);
                    }
                });
                if (result.get()==true) return;
        Stop stop = new Stop(number,in,out,km);
        stop.setStation(byId.get());
        stopRepository.save(stop) ;
    }

    public boolean change(long station, String number,int km,LocalDateTime timein,LocalDateTime timeout) {
        if (timein.isBefore(LocalDateTime.now().plusMinutes(1))) return false;
        if (timeout.isBefore(timein)) return false;
        List<Stop> byTrain = stopRepository.findByTrain(number);
        AtomicBoolean result = new AtomicBoolean(false);
        if (byTrain.size()>0){
            byTrain.forEach(e->{
                if (e.getStation().getId()==station){
                    result.set(true);
                    e.setKm(km);
                    e.setArrival(timein);
                    e.setDeparture(timeout);
                    stopRepository.save(e);
                }
            });
        }
        return result.get();
    }

    public ArrayList <Route> search(String from, String to) {
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
                                String s = calculateTime(list, stop);
                                double value = (stop.getKm()-list.getKm())*list.getPrice();
                                String valueFormer = String.valueOf(value).substring(0,4);
                                value = Double.parseDouble(valueFormer);
                                int seats = list.getSeats();
                                String in = stop.getArrival().toString().replace('T',' ');
                                String out = list.getDeparture().toString().replace('T',' ');
                                routes.add(new Route(list.getTrain(),in,list.getStation().getCity() + ", "
                                        + list.getStation().getStreet(), s  ,out,
                                        stop.getStation().getCity() + ", "+ stop.getStation().getStreet(),
                                        value,seats,list.getId(),stop.getId()));
                            }}}});
            }
        }
        return routes;
    }

    public String calculateTime (Stop first, Stop second){
        long result = ChronoUnit.MINUTES.between(first.getDeparture(), second.getArrival());
        long hours = result/60;
        long minutes = (result-hours*60);
        if (hours>0){
            return hours + " h " + minutes + " m";
        }
        return minutes + " m";

    }

    public List<Route> check(long id,long secondId) {
        Optional<Stop> byId = stopRepository.findById(id);
        Stop stop = byId.get();
        String city = stop.getStation().getCity();
        String routeNumber = stop.getTrain();
        List<Stop> byTrain = stopRepository.findByTrain(routeNumber);
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
            Route route = new Route();
            route.setNumber(el.getTrain());
            route.setStationFirst(el.getStation().getCity() + " , " + el.getStation().getStreet());
            String ar = el.getArrival().toString().replace('T',' ');
            route.setArrival(ar);
            String dep = el.getDeparture().toString().replace('T',' ');
            route.setDeparture(dep);
            route.setKm(el.getKm());
            route.setFirstId(el.getId());
            route.setSecondId(el.getId());
            stops.add(route);
        });
        return stops;
    }


}