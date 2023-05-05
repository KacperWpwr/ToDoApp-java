package application.todoapp.ToDoItem.Request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.sql.Date;
import java.util.Optional;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record EditToDoRequest(@NotNull @NotEmpty Long id, String name, String description,
                              @JsonFormat(pattern="yyyy-MM-dd") Date date) {
}
