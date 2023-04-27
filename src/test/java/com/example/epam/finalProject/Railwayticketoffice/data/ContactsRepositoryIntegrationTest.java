package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.TestConfig;
import com.example.epam.finalProject.Railwayticketoffice.models.Contact;
import com.example.epam.finalProject.Railwayticketoffice.models.Message;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {TestConfig.class})
public class ContactsRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ContactsRepository contactsRepository;

    @Test
    public void findAll() {
        Contact contact = new Contact("Bob","London","street",
                "bob@commun.com","+34641098237");
        entityManager.persist(contact);
        List<Contact> all = (List<Contact>) contactsRepository.findAll();
        boolean test = all.size()>=1;
        Assert.assertTrue(test);

    }
}
