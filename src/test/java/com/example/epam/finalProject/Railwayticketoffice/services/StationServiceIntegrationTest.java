package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.TestConfig;
import com.example.epam.finalProject.Railwayticketoffice.data.StationsRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationServiceIntegrationTest {

    @Autowired
    private StationsRepository stationsRepository;

    @Autowired
    private StationService stationService;

    @Test
    public void testFallAndSuccessAddNewStation (){
        Station station = new Station("Luna","c. Sun 22");
        boolean test = stationService.addNewStation(station);
        Station stationCheck = new Station("Luna","c. Sun 22");
        boolean test2 = stationService.addNewStation(stationCheck);
        stationsRepository.delete(station);
        Assert.assertTrue(test);
        Assert.assertFalse(test2);
    }
}
