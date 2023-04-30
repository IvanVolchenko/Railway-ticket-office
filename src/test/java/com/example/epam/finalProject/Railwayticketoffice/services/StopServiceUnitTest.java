package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.StationsRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.StopRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Route;
import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import com.example.epam.finalProject.Railwayticketoffice.models.Stop;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StopServiceUnitTest {


    private final StopRepository stopRepository = Mockito.mock(StopRepository.class);

    private final StationsRepository stationsRepository = Mockito.mock(StationsRepository.class);


    @InjectMocks
    private StopService stopService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createNewRoute (){
        Stop stop = new Stop();
        stop.setArrival(LocalDateTime.now().plusMinutes(40));
        stop.setDeparture(LocalDateTime.now().plusMinutes(40));
        Stop stop2 = new Stop();
        stop2.setArrival(LocalDateTime.now().plusHours(3));
        stop2.setDeparture(LocalDateTime.now().plusHours(3));
        Station station = new Station();
        ArrayList <Stop> stops = new ArrayList<Stop>();
        stops.add(new Stop());
        when(stationsRepository.findById(any(long.class))).thenReturn(Optional.of(station));
        boolean test = stopService.createNewRoute(stop, stop2, 1000, 999);
        Assert.assertTrue(test);
    }

    @Test
    public void delete (){
        Stop stop = new Stop();
        Station station = new Station("New York","USA");
        station.setId(12);
        stop.setStation(station);
        when(stopRepository.findById(any(long.class))).thenReturn(Optional.of(stop));
        boolean test = stopService.deleteStop(12);
        Assert.assertTrue(test);
    }

    @Test
    public void addStop (){
        String number = UUID.randomUUID().toString();
        Stop stop = new Stop();
        Station station = new Station("New York","USA");
        station.setId(12);
        stop.setStation(station);
        ArrayList <Stop> stops = new ArrayList<Stop>();
        stops.add(stop);
        when(stopRepository.findByTrain(any(String.class))).thenReturn(stops);

        when(stationsRepository.findById(any(long.class))).thenReturn(Optional.of(station));
        boolean test = stopService.addStop(9999,number,200,LocalDateTime.now(),
                LocalDateTime.now().plusHours(2));
        Assert.assertTrue(test);
    }
    @Test
    public void change (){
        Stop stop = new Stop();
        Station station = new Station("New York","USA");
        station.setId(12);
        stop.setStation(station);
        when(stopRepository.findById(any(long.class))).thenReturn(Optional.of(stop));
        boolean test = stopService.change(12,200,LocalDateTime.now().plusMinutes(25),
                LocalDateTime.now().plusHours(2));
        Assert.assertTrue(test);
    }

    @Test
    public void check (){
        String number = UUID.randomUUID().toString();
        Stop stop = new Stop(number,LocalDateTime.now().plusHours(1),LocalDateTime.now().plusHours(4),200);
        Station station = new Station("New York","USA");
        station.setId(12);
        stop.setStation(station);
        ArrayList <Station> stations = new ArrayList<>();
        stations.add(station);
        ArrayList <Stop> stops = new ArrayList<>();
        stops.add(stop);
        when(stopRepository.findById(any(long.class))).thenReturn(Optional.of(stop));
        when(stopRepository.findByTrain(any(String.class))).thenReturn(stops);
        List<Route> routes = stopService.check(12, 12);
        Route route = new Route(number,"2023-04-23 13:24:57.483096800","New York , USA",
                null,"2023-04-23 13:20:20.710429900",null,0.0,0,0,0);
        Assert.assertEquals(String.valueOf(routes.get(0)),route.getStationFirst(),route.getStationFirst());
        Assert.assertEquals(routes.get(0).getNumber(),route.getNumber());
        Assert.assertEquals(routes.size(),1);
    }
}
