package application.todoapp;


import application.todoapp.Security.Authorization.Request.AuthenticateUserRequest;
import application.todoapp.Security.Authorization.Request.CreateUserRequest;
import application.todoapp.Security.Authorization.Response.AuthenticationResponse;
import application.todoapp.ToDoItem.DTO.ToDoListDTO;
import application.todoapp.ToDoItem.Request.CreateToDoRequest;
import application.todoapp.ToDoItem.ToDoItemRepository;
import application.todoapp.User.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import java.sql.Date;
import java.time.LocalDate;

import static application.todoapp.AuthenticationTests.asJsonString;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ToDoAppApplication.class
)
@AutoConfigureMockMvc
public class UserTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ToDoItemRepository itemRepository;
    @Autowired
    private MockMvc mvc;
    private AuthenticateUserRequest auth_request = new AuthenticateUserRequest("kacper@gmail.com", "12345");
    @BeforeEach
    public void prepare_todos() throws Exception{
        CreateUserRequest create_request = new CreateUserRequest("kacper@gmail.com", "12345");
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(create_request)))
                .andExpect(MockMvcResultMatchers.status().is(201));

        CreateToDoRequest create_todo1 = new CreateToDoRequest("kacper@gmail.com","todo1","description", Date.valueOf(LocalDate.now()));
        CreateToDoRequest create_todo2 = new CreateToDoRequest("kacper@gmail.com","todo2","description", Date.valueOf(LocalDate.now()));
        addToDoRequest(create_todo1);
        addToDoRequest(create_todo2);
    }


    @AfterEach
    public void flushAll(){
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    public String authorizeUser() throws Exception{
        String content = mvc.perform(MockMvcRequestBuilders.post("/api/auth/authorize")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(auth_request)))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn().getResponse().getContentAsString();
        return new ObjectMapper().readValue(content, AuthenticationResponse.class).getToken();
    }
    public void addToDoRequest(CreateToDoRequest create_request) throws Exception {
        String token = authorizeUser();

        mvc.perform(MockMvcRequestBuilders.post("/api/todo/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(asJsonString(create_request)))
                .andExpect(MockMvcResultMatchers.status().is(201));
    }

    @Test
    public void getUserToDosTest() throws Exception{
        String token = authorizeUser();
        mvc.perform(MockMvcRequestBuilders.get("/api/user/{email}/todos","kacper@gmail.com")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(result -> Assertions.assertEquals(
                        new ObjectMapper()
                                .readValue(result.getResponse()
                                        .getContentAsString(), ToDoListDTO.class).getItems()
                                .size(),
                        2
                ));
    }

    @Test
    public void getUserToDosWrongEmail() throws Exception{
        String token = authorizeUser();
        mvc.perform(MockMvcRequestBuilders.get("/api/user/{email}/todos","kacper1@gmail.com")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

}
