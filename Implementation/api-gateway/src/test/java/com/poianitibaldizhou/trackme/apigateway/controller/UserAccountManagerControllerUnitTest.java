package com.poianitibaldizhou.trackme.apigateway.controller;

import com.poianitibaldizhou.trackme.apigateway.assembler.UserAssembler;
import com.poianitibaldizhou.trackme.apigateway.entity.User;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentSsnException;
import com.poianitibaldizhou.trackme.apigateway.exception.AlreadyPresentUsernameException;
import com.poianitibaldizhou.trackme.apigateway.exception.UserNotFoundException;
import com.poianitibaldizhou.trackme.apigateway.service.UserAccountManagerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Individual test of the controller
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserAccountManagerController.class)
@Import({UserAssembler.class})
public class UserAccountManagerControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserAccountManagerService service;

    /**
     * Test the retrieval of information related to a user by means of his ssn
     *
     * @throws Exception due to mock mvc method get
     */
    @Test
    public void testGetUserBySsn() throws Exception {
        User user = new User();
        user.setSsn("user1");
        user.setBirthNation("Italy");
        user.setBirthCity("Verona");
        user.setBirthDate(new Date(0));
        user.setFirstName("firstname1");
        user.setLastName("lastname1");
        user.setPassword("password1");
        user.setUserName("username1");

        given(service.getUserBySsn("user1")).willReturn(user);

        mvc.perform(get("/useraccountservice/users/user1").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("password", is(user.getPassword())))
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("lastName", is(user.getLastName())))
                .andExpect(jsonPath("birthDate", is(user.getBirthDate().toString())))
                .andExpect(jsonPath("birthCity", is(user.getBirthCity())))
                .andExpect(jsonPath("birthNation", is(user.getBirthNation())))
                .andExpect(jsonPath("userName", is(user.getUserName())));
    }

    /**
     * Test the retrieval of information related to a user by means of his ssn when no user with that ssn
     * is registered
     *
     * @throws Exception due to mock mvc method get
     */
    @Test
    public void testGetUserBySsnWhenNotPresent() throws Exception {
        User user = new User();
        user.setSsn("user1");
        user.setBirthNation("Italy");
        user.setBirthCity("Verona");
        user.setBirthDate(new Date(0));
        user.setFirstName("firstname1");
        user.setLastName("lastname1");
        user.setPassword("password1");
        user.setUserName("username1");

        given(service.getUserBySsn("user1")).willThrow(new UserNotFoundException(user.getSsn()));

        mvc.perform(get("/useraccountservice/users/user1").accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isNotFound());
    }

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
        user.setUserName("vertex95");

        given(service.registerUser(any(User.class))).willReturn(user);

        String json = "{\n" +
                "   \"password\":\"tangpass\",\n" +
                "   \"userName\":\"vertex95\",\n" +
                "   \"firstName\":\"TangTang\",\n" +
                "   \"lastName\":\"Zhou\",\n" +
                "   \"birthDate\":\"1970-01-01\",\n" +
                "   \"birthCity\":\"Brescia\",\n" +
                "   \"birthNation\":\"Italia\"\n" +
                "}";

        mvc.perform(post("/useraccountservice/users/newUser").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("password", is(user.getPassword())))
                .andExpect(jsonPath("userName", is(user.getUserName())))
                .andExpect(jsonPath("firstName", is(user.getFirstName())))
                .andExpect(jsonPath("lastName", is(user.getLastName())))
                .andExpect(jsonPath("birthDate", is(user.getBirthDate().toString())))
                .andExpect(jsonPath("birthCity", is(user.getBirthCity())))
                .andExpect(jsonPath("birthNation", is(user.getBirthNation())))
                .andExpect(jsonPath("_links.self.href", is("http://localhost/useraccountservice/users/"+user.getSsn())));
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
        user.setUserName("alreadyPresentUsername");

        String json = "{\n" +
                "   \"password\":\"tangpass\",\n" +
                "   \"userName\":\"alreadyPresentUsername\",\n" +
                "   \"firstName\":\"TangTang\",\n" +
                "   \"lastName\":\"Zhou\",\n" +
                "   \"birthDate\":\"1970-01-01\",\n" +
                "   \"birthCity\":\"Brescia\",\n" +
                "   \"birthNation\":\"Italia\"\n" +
                "}";

        given(service.registerUser(any(User.class))).willThrow(new AlreadyPresentUsernameException(user.getUserName()));

        mvc.perform(post("/useraccountservice/users/newUser").
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
        user.setUserName("newUserName");

        String json = "{\n" +
                "   \"password\":\"tangpass\",\n" +
                "   \"userName\":\"newUserName\",\n" +
                "   \"firstName\":\"TangTang\",\n" +
                "   \"lastName\":\"Zhou\",\n" +
                "   \"birthDate\":\"1970-01-01\",\n" +
                "   \"birthCity\":\"Brescia\",\n" +
                "   \"birthNation\":\"Italia\"\n" +
                "}";

        given(service.registerUser(any(User.class))).willThrow(new AlreadyPresentSsnException(user.getSsn()));

        mvc.perform(post("/useraccountservice/users/SsnAlreadyPresent").
                contentType(MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8").content(json))
                .andExpect(status().isBadRequest());

    }
}
