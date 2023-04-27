package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.TestConfig;
import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAddNewUser (){
        User user = new User("Pop","Popov","popov123",
                "popovPopovPop@mail.com","password");
        boolean test = userService.addNewUser(user);
        Optional<User> userCheck = userRepository.findByUsername(user.getUsername());
        userRepository.deleteById(user.getId());
        Assert.assertTrue(test);
        Assert.assertTrue(userCheck.isPresent());
    }

    @Test
    public void testChange (){
        User user = new User("Pop","Popov","popov123",
                "popovPopovPop@mail.com","password");
        boolean test = userService.addNewUser(user);
        String number="popov888888";
        user.setDocumentNumber(number);
        boolean change = userService.change(user);
        Optional<User> userCheck = userRepository.findById(user.getId());
        userRepository.deleteById(user.getId());
        Assert.assertEquals(user.getDocumentNumber(),userCheck.get().getDocumentNumber());
        Assert.assertEquals(number,userCheck.get().getDocumentNumber());
        Assert.assertTrue(test);
        Assert.assertTrue(change);
    }

}
