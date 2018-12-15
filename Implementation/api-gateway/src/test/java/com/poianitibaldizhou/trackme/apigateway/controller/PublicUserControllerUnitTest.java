package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.assembler.UserAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.apigateway.security.TokenAuthenticationProvider;
import com.poianitibaldizhou.trackme.apigateway.security.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAccountManagerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test of the public controller of the users
 */
@RunWith(SpringRunner.class)
@WebMvcTest(PublicUserController.class)
@Import({UserAssembler.class})
public class PublicUserControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserAccountManagerService service;


    // unused but needed to set up the context application by Spring

    @MockBean
    private UserAuthenticationService authenticationService;

    @MockBean
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    /**
     * Test the registration of a user
     *
     * @throws Exception due to mvc post method
     */
    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setSsn("newUser");
        user.setBirthNation("Italia");
        user.setBirthCity("Brescia");
        user.setBirthDate(new Date(0));
        user.setFirstName("TangTang");
        user.setLastName("Zhou");
        user.setPassword("tangpass");
        user.setUsername("vertex95");

        given(service.registerUser(any(User.class))).willReturn(user);

        String json = "{\n" +
                "   \"password\":\"tangpass\",\n" +
                "   \"username\":\"vertex95\",\n" +
                "   \"firstName\":\"TangTang\",\n" +
                "   \"lastName\":\"Zhou\",\n" +
                "   \"birthDate\":\"1970-01-01\",\n" +
                "   \"birthCity\":\"Brescia\",\n" +
                "   \"birthNation\":\"Italia\"\n" +
                "}";

        mvc.perform(post("/public/users/newUser").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("password", is(user.getPassword())))
                .andExpect(jsonPath("username", is(user.getUsername())))
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("lastName", is(user.getLastName())))
                .andExpect(jsonPath("birthDate", is(user.getBirthDate().toString())))
                .andExpect(jsonPath("birthCity", is(user.getBirthCity())))
                .andExpect(jsonPath("birthNation", is(user.getBirthNation())))
                .andExpect(jsonPath("_links.self.href", is("http://localhost/users/info")));
    }

    /**
     * Test the registration of a user when another user with the specified username is already registered
     *
     * @throws Exception due to mvc mock post method
     */
    @Test
    public void testUserRegistrationWhenUserNameAlreadyPresent() throws Exception {
        User user = new User();
        user.setSsn("newUser");
        user.setBirthNation("Italia");
        user.setBirthCity("Brescia");
        user.setBirthDate(new Date(0));
        user.setFirstName("TangTang");
        user.setLastName("Zhou");
        user.setPassword("tangpass");
        user.setUsername("alreadyPresentUsername");

        String json = "{\n" +
                "   \"password\":\"tangpass\",\n" +
                "   \"username\":\"alreadyPresentUsername\",\n" +
                "   \"firstName\":\"TangTang\",\n" +
                "   \"lastName\":\"Zhou\",\n" +
                "   \"birthDate\":\"1970-01-01\",\n" +
                "   \"birthCity\":\"Brescia\",\n" +
                "   \"birthNation\":\"Italia\"\n" +
                "}";

        given(service.registerUser(any(User.class))).willThrow(new AlreadyPresentUsernameException(user.getUsername()));

        mvc.perform(post("/public/users/newUser").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());

    }

    /**
     * Test the registration of a user when another user with the specified ssn is already registered
     *
     * @throws Exception due to mvc mock post method
     */
    @Test
    public void testUserRegistrationWhenSsnAlreadyPresent() throws Exception {
        User user = new User();
        user.setSsn("SsnAlreadyPresent");
        user.setBirthNation("Italia");
        user.setBirthCity("Brescia");
        user.setBirthDate(new Date(0));
        user.setFirstName("TangTang");
        user.setLastName("Zhou");
        user.setPassword("tangpass");
        user.setUsername("newUserName");

        String json = "{\n" +
                "   \"password\":\"tangpass\",\n" +
                "   \"username\":\"newUserName\",\n" +
                "   \"firstName\":\"TangTang\",\n" +
                "   \"lastName\":\"Zhou\",\n" +
                "   \"birthDate\":\"1970-01-01\",\n" +
                "   \"birthCity\":\"Brescia\",\n" +
                "   \"birthNation\":\"Italia\"\n" +
                "}";

        given(service.registerUser(any(User.class))).willThrow(new AlreadyPresentSsnException(user.getSsn()));

        mvc.perform(post("/public/users/SsnAlreadyPresent").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());

    }
}
