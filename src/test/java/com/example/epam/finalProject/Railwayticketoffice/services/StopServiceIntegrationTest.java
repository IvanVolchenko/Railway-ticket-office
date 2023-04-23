package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.StopRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Stop;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StopServiceIntegrationTest {

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private StopService stopService;

    @Test
    public void fallCreateNewRoute (){
        Stop stop = new Stop();
        stop.setArrival(LocalDateTime.now().minusDays(1));
        stop.setDeparture(LocalDateTime.now().minusDays(1));
        Stop stop2 = new Stop();
        stop2.setArrival(LocalDateTime.now().plusMinutes(5));
        stop2.setDeparture(LocalDateTime.now().plusMinutes(5));
        boolean test = stopService.createNewRoute(stop, stop2, 1000, 999);
        Assert.assertFalse(test);
    }

    @Test
    public void fallDelete (){
        String number = UUID.randomUUID().toString();
        boolean test = stopService.delete(9999, number);
        Assert.assertFalse(test);
    }

    @Test
    public void fallAddStop (){
        String number = UUID.randomUUID().toString();
        boolean testTime = stopService.addStop(9999,number,200,LocalDateTime.now(),
                LocalDateTime.now().minusHours(2));
        boolean testStationOrNumber = stopService.addStop(9999,number,200,
                LocalDateTime.now().plusHours(5), LocalDateTime.now().plusMonths(1));
        Assert.assertFalse(testTime);
        Assert.assertFalse(testStationOrNumber);
    }

    @Test
    public void fallChange (){
        String number = UUID.randomUUID().toString();
        boolean testTime = stopService.change(9999,number,200,LocalDateTime.now(),
                LocalDateTime.now().minusHours(2));
        boolean testStationOrNumber = stopService.change(9999,number,200,
                LocalDateTime.now().plusHours(5), LocalDateTime.now().plusMonths(1));
        Assert.assertFalse(testTime);
        Assert.assertFalse(testStationOrNumber);
    }

    @Test
    public void calculateTime (){
        Stop stop1 = new Stop();
        stop1.setDeparture(LocalDateTime.now());
        Stop stop2 = new Stop();
        stop2.setArrival(LocalDateTime.now().plusDays(1).plusHours(5).plusMinutes(10));
        String result = stopService.calculateTime(stop1, stop2);
        stop1.setDeparture(LocalDateTime.now().plusMinutes(10));
        stop2.setArrival(LocalDateTime.now().plusMinutes(59));
        String result2 = stopService.calculateTime(stop1, stop2);
        Assert.assertEquals("29 h 10 m",result);
        Assert.assertEquals("49 m",result2);
    }
}
