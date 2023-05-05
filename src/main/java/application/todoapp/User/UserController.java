package application.todoapp.User;

import application.todoapp.ToDoItem.DTO.ToDoListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/{email}/todos")
    public ResponseEntity<ToDoListDTO> getUserToDos(@PathVariable String email){
        return new ResponseEntity<>(userService.getUserToDos(email), HttpStatusCode.valueOf(200));
    }
}
