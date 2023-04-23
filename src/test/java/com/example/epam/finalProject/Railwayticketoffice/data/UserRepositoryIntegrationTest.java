package com.example.epam.finalProject.Railwayticketoffice.data;

import com.example.epam.finalProject.Railwayticketoffice.models.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername() {
        User user = new User("Ivan","Ivanov","Y1229112U",
                "Ivanov22@mymail.rcom","password");
        user.setAuthorities("USER");
        user.setUsername(user.getEmailAddress());
        entityManager.persist(user);
        Optional<User> person = userRepository.findByUsername("Ivanov22@mymail.rcom");
        User userFound = person.get();
        Assert.assertEquals(userFound.getFirstName(),user.getFirstName());
        Assert.assertEquals(userFound.getLastName(),user.getLastName());
        Assert.assertEquals(userFound.getDocumentNumber(),user.getDocumentNumber());
    }

    @Test
    public void findByEmailAddress() {
        User user = new User("Ivan","Ivanov","Y1229112U",
                "Ivanov22@mymail.rcom","password");
        user.setAuthorities("USER");
        user.setUsername(user.getEmailAddress());
        entityManager.persist(user);
        Optional<User> person = userRepository.findByEmailAddress("Ivanov22@mymail.rcom");
        User userFound = person.get();
        Assert.assertEquals(userFound.getFirstName(),user.getFirstName());
        Assert.assertEquals(userFound.getLastName(),user.getLastName());
        Assert.assertEquals(userFound.getDocumentNumber(),user.getDocumentNumber());
    }

    @Test
    public void findByDocumentNumber() {
        User user = new User("Ivan","Ivanov","Y1229112U",
                "Ivanov22@mymail.rcom","password");
        user.setAuthorities("USER");
        user.setUsername(user.getEmailAddress());
        entityManager.persist(user);
        Optional<User> person = userRepository.findByDocumentNumber("Y1229112U");
        User userFound = person.get();
        Assert.assertEquals(userFound.getFirstName(),user.getFirstName());
        Assert.assertEquals(userFound.getLastName(),user.getLastName());
        Assert.assertEquals(userFound.getEmailAddress(),user.getEmailAddress());
    }
}
