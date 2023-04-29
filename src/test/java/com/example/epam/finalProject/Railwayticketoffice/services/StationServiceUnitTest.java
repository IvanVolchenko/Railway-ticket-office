package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.StationsRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.StopRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationServiceUnitTest {

    private final StationsRepository stationsRepository = Mockito.mock(StationsRepository.class);


    @InjectMocks
    private StationService stationService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addNewStationFalse (){
        Station station = new Station("Luna","c. Sun 22");
        ArrayList<Station> stationList = new ArrayList<>();
        stationList.add(station);
        when(stationsRepository.findAllByCity(any(String.class))).thenReturn(stationList);
        when(stationsRepository.findAllByStreet(any(String.class))).thenReturn(stationList);
        boolean result = stationService.addNewStation(station);
        Assert.assertFalse(result);
    }

    @Test
    public void addNewStationTrue (){
        Station station = new Station("Luna","c. Sun 22");
        Station station2 = new Station("Luna","c. Raul 23");
        ArrayList<Station> stationList = new ArrayList<>();
        stationList.add(station);
        ArrayList<Station> stationList2 = new ArrayList<>();
        stationList2.add(station2);
        when(stationsRepository.findAllByCity(any(String.class))).thenReturn(stationList);
        when(stationsRepository.findAllByStreet(any(String.class))).thenReturn(stationList2);
        boolean result = stationService.addNewStation(station);
        Assert.assertTrue(result);
    }
}
