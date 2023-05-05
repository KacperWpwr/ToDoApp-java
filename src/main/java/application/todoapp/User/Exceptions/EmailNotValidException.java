package application.todoapp.User.Exceptions;

import application.todoapp.ApplicationException;

public class EmailNotValidException extends UserException {
    public EmailNotValidException() {
        super("Given email is not valid", 400);
    }
}
