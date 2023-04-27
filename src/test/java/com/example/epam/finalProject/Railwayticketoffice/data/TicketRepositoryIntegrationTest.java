package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.RailwayTicketOfficeApplication;
import com.example.epam.finalProject.Railwayticketoffice.TestConfig;
import com.example.epam.finalProject.Railwayticketoffice.config.MvcConfig;
import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {TestConfig.class})
//@PropertySource("classpath:/lang/messages.properties")
public class TicketRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    public void findByDocument() {
        Ticket ticket = new Ticket("Y1229112U",120, UUID.randomUUID().toString(),"Ivan",
                "H777777W","24.02.2024","25.02.2024","25.02.2024");
        entityManager.persist(ticket);
        List<Ticket> tickets = ticketRepository.findByDocument("H777777W");
        Assert.assertEquals(1,tickets.size());
        Assert.assertEquals(ticket.getName(),tickets.get(0).getName());
    }

    @Test
    public void findAllByTran() {
        Ticket ticket = new Ticket("Y1229112U",120, UUID.randomUUID().toString(),"Ivan",
                "H777777W","24.02.2024","25.02.2024","25.02.2024");
        Ticket ticket2 = new Ticket("Y1229112U",124, UUID.randomUUID().toString(),"Bob",
                "O222P","24.02.2024","25.02.2024","25.02.2024");
        entityManager.persist(ticket);
        entityManager.persist(ticket2);
        List<Ticket> tickets = ticketRepository.findAllByTran("Y1229112U");
        Assert.assertEquals(2,tickets.size());
        Assert.assertEquals(ticket.getName(),tickets.get(0).getName());
        Assert.assertEquals(ticket2.getName(),tickets.get(1).getName());
        Assert.assertEquals(ticket.getDocument(),tickets.get(0).getDocument());
        Assert.assertEquals(ticket2.getDocument(),tickets.get(1).getDocument());
    }
}
