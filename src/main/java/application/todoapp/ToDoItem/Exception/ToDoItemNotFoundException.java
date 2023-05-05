package application.todoapp.ToDoItem.Exception;

public class ToDoItemNotFoundException extends ToDoItemException{
    public ToDoItemNotFoundException() {
        super("Item not found", 404);
    }
}
