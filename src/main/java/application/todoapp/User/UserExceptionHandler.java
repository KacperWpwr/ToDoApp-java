package application.todoapp.User;

import application.todoapp.User.Exceptions.UserException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<String> handle(UserException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatusCode.valueOf(exception.getCode()));
    }
}
