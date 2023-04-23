package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import com.example.epam.finalProject.Railwayticketoffice.models.Stop;
import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class StopRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StopRepository stopRepository;
    @Autowired
    private StationsRepository stationsRepository;

    @Test
    public void findByTrain() {
        Stop stop1 = new Stop("Y1229112U", LocalDateTime.now().plusHours(1),LocalDateTime.now().plusHours(2),
                180);
        Stop stop2 = new Stop("Y1229112U", LocalDateTime.now().plusHours(3),LocalDateTime.now().plusHours(4),
                600);
        entityManager.persist(stop1);
        entityManager.persist(stop2);
        List<Stop> stops = stopRepository.findByTrain("Y1229112U");
        Assert.assertEquals(2,stops.size());
        Assert.assertEquals(stop1.getKm(),stops.get(0).getKm());
        Assert.assertEquals(stop2.getKm(),stops.get(1).getKm());
        Assert.assertEquals(stop1.getArrival(),stops.get(0).getArrival());
        Assert.assertEquals(stop2.getArrival(),stops.get(1).getArrival());
    }

    @Test
    public void findAllByStation_id() {
        Stop stop1 = new Stop("Y1229112U", LocalDateTime.now().plusHours(1),LocalDateTime.now().plusHours(2),
                180);
        Station station = new Station("New","York");
        Stop stop2 = new Stop("CC23333G", LocalDateTime.now().plusHours(3),LocalDateTime.now().plusHours(4),
                600);
        entityManager.persist(station);
        Optional<Station> york = stationsRepository.findByStreet("York");
        Station station2 = york.get();
        stop1.setStation(station2);
        stop2.setStation(station2);
        entityManager.persist(stop1);
        entityManager.persist(stop2);
        List<Stop> stops = stopRepository.findAllByStation_id(station.getId());
        Assert.assertEquals(2,stops.size());
        Assert.assertEquals(stop1.getKm(),stops.get(0).getKm());
        Assert.assertEquals(stop2.getKm(),stops.get(1).getKm());
        Assert.assertEquals(stop1.getArrival(),stops.get(0).getArrival());
        Assert.assertEquals(stop2.getArrival(),stops.get(1).getArrival());
    }

    @Test
    public void findAllByTrain() {
        Stop stop1 = new Stop("Y1229112U", LocalDateTime.now().plusHours(1),LocalDateTime.now().plusHours(2),
                180);
        Station station = new Station("New","York");
        Stop stop2 = new Stop("Y1229112U", LocalDateTime.now().plusHours(3),LocalDateTime.now().plusHours(4),
                300);
        entityManager.persist(station);
        Optional<Station> york = stationsRepository.findByStreet("York");
        Station station2 = york.get();
        stop1.setStation(station2);
        stop2.setStation(station2);
        entityManager.persist(stop1);
        entityManager.persist(stop2);
        List<Stop> stops = stopRepository.findAllByTrain(stop1.getTrain());
        Assert.assertEquals(stops.size(),2);
        Assert.assertEquals(stop1.getKm(),stops.get(0).getKm());
        Assert.assertEquals(stop2.getKm(),stops.get(1).getKm());
        Assert.assertEquals(stop1.getArrival(),stops.get(0).getArrival());
        Assert.assertEquals(stop2.getArrival(),stops.get(1).getArrival());
    }
}
