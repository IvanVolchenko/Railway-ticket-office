package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.Message;
import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class MessageRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void findAll() {
        Message message = new Message("Bob","+34641098237","Thank's for your job");
        entityManager.persist(message);
        List<Message> all = (List<Message>) messageRepository.findAll();
        boolean test = all.size()>=1;
        Assert.assertTrue(test);

    }
}
