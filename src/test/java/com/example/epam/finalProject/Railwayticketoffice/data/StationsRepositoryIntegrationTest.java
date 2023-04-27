package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.TestConfig;
import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {TestConfig.class})
public class StationsRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StationsRepository stationsRepository;

    @Test
    public void findByStreet() {
        Station station = new Station("New","York");
        entityManager.persist(station);
        Optional<Station> york = stationsRepository.findByStreet("York");
        Station station2 = york.get();
        Assert.assertEquals(station.getCity(),station2.getCity());
        Assert.assertEquals(station.getStreet(),station2.getStreet());
    }

    @Test
    public void findAllByCity() {
        Station station = new Station("New","York");
        Station station2 = new Station("New","Manh");
        Station station3 = new Station("New","Angel");
        entityManager.persist(station);
        entityManager.persist(station2);
        entityManager.persist(station3);
        ArrayList<Station> allStations = stationsRepository.findAllByCity("New");
        Assert.assertEquals(3,allStations.size());
    }

    @Test
    public void findAllByStreet() {
        Station station = new Station("New","York");
        Station station2 = new Station("Pol","York");
        Station station3 = new Station("Manh","York");
        entityManager.persist(station);
        entityManager.persist(station2);
        entityManager.persist(station3);
        ArrayList<Station> allStations = stationsRepository.findAllByStreet("York");
        Assert.assertEquals(3,allStations.size());
    }
}
