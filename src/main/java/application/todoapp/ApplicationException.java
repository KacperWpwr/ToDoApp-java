package application.todoapp;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class ApplicationException extends RuntimeException{
    private final String message;
    private final Integer code;

    public ApplicationException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}
