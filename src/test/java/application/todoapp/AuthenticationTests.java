package application.todoapp;

import application.todoapp.Security.Authorization.Request.AuthenticateUserRequest;
import application.todoapp.Security.Authorization.Request.CreateUserRequest;
import application.todoapp.User.UserRepository;
import application.todoapp.User.UserService;



import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ToDoAppApplication.class
)
@AutoConfigureMockMvc
public class AuthenticationTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mvc;


    @AfterEach
    public void flush(){
        userRepository.deleteAll();
    }

    @Test
    public void createUserTest() throws Exception{
        CreateUserRequest create_request = new CreateUserRequest("kacper@gmail.com", "12345");

        mvc.perform(MockMvcRequestBuilders.post("/api/auth/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(create_request)))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn().getRequest();
    }

    @Test
    public void emailConflictTest() throws Exception{
        CreateUserRequest create_request = new CreateUserRequest("kacper@gmail.com", "12345");
        CreateUserRequest create_request2 = new CreateUserRequest("kacper@gmail.com", "123456");
        System.out.println("First user");
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(create_request)))
                .andExpect(MockMvcResultMatchers.status().is(201));
        System.out.println("Second user");
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(create_request2)))
                .andExpect(MockMvcResultMatchers.status().is(409));


    }

    @Test
    public void emailNotValidTest() throws Exception{
        CreateUserRequest create_request = new CreateUserRequest("kacper@", "12345");
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(create_request)))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void authenticationTest() throws Exception{
        CreateUserRequest create_request = new CreateUserRequest("kacper@gmail.com", "12345");

        mvc.perform(MockMvcRequestBuilders.post("/api/auth/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(create_request)))
                .andExpect(MockMvcResultMatchers.status().is(201));


        AuthenticateUserRequest auth_request = new AuthenticateUserRequest("kacper@gmail.com", "12345");
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/authorize")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(auth_request)))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
