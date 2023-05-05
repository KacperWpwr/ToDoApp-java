package application.todoapp.User.Exceptions;

public class EmailTakenException extends UserException{
    public EmailTakenException() {
        super("Given email is already taken", 409);
    }
}
