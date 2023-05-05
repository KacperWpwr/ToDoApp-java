package application.todoapp.User.Exceptions;

public class InvalidPasswordException extends UserException{
    public InvalidPasswordException() {
        super("Invalid password", 403);
    }
}
