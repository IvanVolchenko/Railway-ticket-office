package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFallAddNewUser (){
        User user = new User("Pop","Popov","popov123",
                "popovPopovPop@mail.com","password");
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(user));
        when(userRepository.findByDocumentNumber(any(String.class))).thenReturn(Optional.of(user));

        boolean test = userService.addNewUser(user);
        Assert.assertFalse(test);
    }

    @Test
    public void testFallChange (){
        User user = new User("Pop","Popov","popov123",
                "popovPopovPop@mail.com","password");
        user.setId(18);
        User userSecond = new User("Lo","Lio","Lio001",
                "Lio1999@mail.com","password");
        userSecond.setId(120);
        when(userRepository.findByEmailAddress(any(String.class))).thenReturn(Optional.of(userSecond));
        when(userRepository.findByDocumentNumber(any(String.class))).thenReturn(Optional.of(userSecond));

        boolean test = userService.change(user);
        Assert.assertFalse(test);
    }

}
