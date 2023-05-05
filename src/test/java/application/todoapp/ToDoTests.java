package application.todoapp;


import application.todoapp.Security.Authorization.Request.AuthenticateUserRequest;
import application.todoapp.Security.Authorization.Request.CreateUserRequest;
import application.todoapp.Security.Authorization.Response.AuthenticationResponse;
import application.todoapp.ToDoItem.DTO.ToDoListDTO;
import application.todoapp.ToDoItem.Request.CreateToDoRequest;
import application.todoapp.ToDoItem.Request.EditToDoRequest;
import application.todoapp.ToDoItem.ToDoItem;
import application.todoapp.ToDoItem.ToDoItemRepository;
import application.todoapp.ToDoItem.ToDoItemService;
import application.todoapp.User.User;
import application.todoapp.User.UserRepository;
import application.todoapp.User.UserService;

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
import java.util.Enumeration;

import static application.todoapp.AuthenticationTests.asJsonString;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = ToDoAppApplication.class
)
@AutoConfigureMockMvc
public class ToDoTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ToDoItemRepository itemRepository;
    @Autowired
    private MockMvc mvc;

    private AuthenticateUserRequest auth_request = new AuthenticateUserRequest("kacper@gmail.com", "12345");

    private ToDoItem test_item = ToDoItem.builder()
            .title("todo1")
            .description("description")
            .date(Date.valueOf(LocalDate.now()))
            .build();
    @BeforeEach
    public void addUser() throws Exception{

        CreateUserRequest create_request = new CreateUserRequest("kacper@gmail.com", "12345");
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(create_request)))
                .andExpect(MockMvcResultMatchers.status().is(201));
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
    public Long addToDoRequest() throws Exception{
        String token  = authorizeUser();


        CreateToDoRequest create_request = new CreateToDoRequest("kacper@gmail.com", test_item.getTitle(), test_item.getDescription(), test_item.getDate());

        return new ObjectMapper().readValue(
                mvc.perform(MockMvcRequestBuilders.post("/api/todo/create")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization","Bearer "+token)
                                .content(asJsonString(create_request)))
                        .andExpect(MockMvcResultMatchers.status().is(201))
                        .andReturn().getResponse().getContentAsString(),
                ToDoListDTO.class
        ).getItems().get(0).getId();
    }



    @Test
    public void ToDoAddEndpointTest() throws Exception{

        String token  = authorizeUser();


        CreateToDoRequest create_request = new CreateToDoRequest("kacper@gmail.com", test_item.getTitle(), test_item.getDescription(), test_item.getDate());

        //Edit To Do Request
        mvc.perform(MockMvcRequestBuilders.post("/api/todo/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+token)
                .content(asJsonString(create_request)))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(result -> new ObjectMapper()
                        .readValue(result.getResponse()
                                .getContentAsString(), ToDoListDTO.class).getItems()
                                .get(0).getName().equals(test_item.getTitle()));
    }

    @Test
    public void AddToDoUserNotPresentEndpointTest() throws Exception{
        String token  = authorizeUser();
        CreateToDoRequest create_request = new CreateToDoRequest("kacper1@gmail.com", test_item.getTitle(), test_item.getDescription(), test_item.getDate());

        //Edit To Do Request
        mvc.perform(MockMvcRequestBuilders.post("/api/todo/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token)
                        .content(asJsonString(create_request)))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(result -> Assertions.assertEquals(result.getResponse().getContentAsString(),"User not found"));

    }

    @Test
    public void ToDoEditEndpointTest() throws Exception{
        Long todo_id = addToDoRequest();
        EditToDoRequest edit_request = EditToDoRequest.builder()
                .id(todo_id)
                .name("todo_edit")
                .build();

        String token  = authorizeUser();

        //Edit To Do Request
        mvc.perform(MockMvcRequestBuilders.put("/api/todo/edit")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer "+token)
                .content(asJsonString(edit_request)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(result -> new ObjectMapper()
                        .readValue(result.getResponse().getContentAsString(), ToDoListDTO.class)
                        .getItems().get(0).getName().equals(edit_request.name())

                );
    }

    @Test
    public void ToDoEditWrongIdEndpointTest() throws Exception{
        Long todo_id = addToDoRequest();
        EditToDoRequest edit_request = EditToDoRequest.builder()
                .id(todo_id+1L) //Wrong id
                .name("todo_edit")
                .build();

        String token  = authorizeUser();

        //Edit To Do Request
        mvc.perform(MockMvcRequestBuilders.put("/api/todo/edit")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token)
                        .content(asJsonString(edit_request)))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(result -> Assertions.assertEquals(result.getResponse().getContentAsString(), "Item not found"));
    }
    @Test
    public void ToDoDeleteEndpointEdit() throws Exception{
        Long todo_id = addToDoRequest();
        String token  = authorizeUser();


        //Delete To Do Request
        mvc.perform(MockMvcRequestBuilders.delete("/api/todo/delete/{todo_id}",todo_id)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(result ->
                    Assertions.assertEquals(new ObjectMapper().readValue(result.getResponse().getContentAsString(), ToDoListDTO.class)
                            .getItems().size(),0)
                );
    }

    @Test
    public void ToDoDeleteWrongIdEndpointTest() throws Exception{
        Long todo_id = addToDoRequest();
        String token  = authorizeUser();

        //Edit To Do Request
        mvc.perform(MockMvcRequestBuilders.delete("/api/todo/delete/{todo_id}",todo_id+3L)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(result -> Assertions.assertEquals(result.getResponse().getContentAsString(), "Item not found"));
    }




}
