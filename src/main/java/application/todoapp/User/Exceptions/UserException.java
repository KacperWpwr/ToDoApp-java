package application.todoapp.User.Exceptions;

import application.todoapp.ApplicationException;

public abstract class UserException extends ApplicationException {

    public UserException(String message, Integer code) {
        super(message, code);
    }
}
