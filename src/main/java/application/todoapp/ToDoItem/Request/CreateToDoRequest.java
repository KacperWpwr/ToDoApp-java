package application.todoapp.ToDoItem.Request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record CreateToDoRequest(@NotNull @NotEmpty String user_email,
                                @NotNull @NotEmpty String name,
                                @NotNull  String description,
                                @NotNull @NotEmpty @JsonFormat(pattern="yyyy-MM-dd") Date date){
}
