package application.todoapp.User.Exceptions;

public class UserNotFoundException extends UserException{
    public UserNotFoundException() {
        super("User not found", 404);
    }
}
