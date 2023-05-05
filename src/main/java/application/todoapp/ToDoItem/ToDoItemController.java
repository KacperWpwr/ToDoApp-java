package application.todoapp.ToDoItem;

import application.todoapp.ToDoItem.DTO.ToDoListDTO;
import application.todoapp.ToDoItem.Request.CreateToDoRequest;
import application.todoapp.ToDoItem.Request.EditToDoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todo")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class ToDoItemController {
    private final ToDoItemService itemService;

    @PostMapping("/create")
    public ResponseEntity<ToDoListDTO> createToDoItem(@RequestBody CreateToDoRequest request){
        return new ResponseEntity<>(itemService.create(request), HttpStatusCode.valueOf(201));
    }
    @PutMapping("/edit")
    public ResponseEntity<ToDoListDTO> editToDoItem(@RequestBody EditToDoRequest request){
        return new ResponseEntity<>(itemService.edit(request), HttpStatusCode.valueOf(200));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ToDoListDTO> deleteToDoItem(@PathVariable Long id){
        return new ResponseEntity<>(itemService.delete(id), HttpStatusCode.valueOf(200));
    }
}
