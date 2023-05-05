package application.todoapp.ToDoItem.Exception;

import application.todoapp.ApplicationException;

public class ToDoItemException extends ApplicationException {
    public ToDoItemException(String message, Integer code) {
        super(message, code);
    }
}
