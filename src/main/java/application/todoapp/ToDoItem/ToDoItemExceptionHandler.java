package application.todoapp.ToDoItem;

import application.todoapp.ToDoItem.Exception.ToDoItemException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ToDoItemExceptionHandler {

    @ExceptionHandler(value = ToDoItemException.class)
    public ResponseEntity<String> handle(ToDoItemException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatusCode.valueOf(exception.getCode()));
    }
}
