package com.example.epam.finalProject.Railwayticketoffice;

import com.example.epam.finalProject.Railwayticketoffice.controllers.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GeneralTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;
    @Autowired
    private MyProfileController myProfileController;
    @Autowired
    private AdminController adminController;
    @Autowired
    private AdminContactsController adminContactsController;
    @Autowired
    private AdminRoutesController adminRoutesController;
    @Autowired
    private AdminTripController adminTripController;

    @Test
    public void testControllersExist() {
        Assert.assertNotNull(mainController);
        Assert.assertNotNull(myProfileController);
        Assert.assertNotNull(adminController);
        Assert.assertNotNull(adminContactsController);
        Assert.assertNotNull(adminRoutesController);
        Assert.assertNotNull(adminTripController);
    }

    @Test
    public void test() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print())
                        .andExpect(status().isOk()).andExpect(content().string(containsString("Barcelona")));
    }

    @Test
    public void loginTest() throws Exception{
        this.mockMvc.perform(get("/profile"))
                .andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void correctLoginTest() throws Exception{
        this.mockMvc.perform(formLogin().user("adminSpT@gmal.com").password("password"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void badAdmin() throws Exception {
        this.mockMvc.perform(get("/admin"))
                .andDo(print()).andExpect(status().is3xxRedirection());
    }
}
