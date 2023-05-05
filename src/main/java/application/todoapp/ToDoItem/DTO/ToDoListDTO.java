package application.todoapp.ToDoItem.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class ToDoListDTO {
    @NotNull
    private List<ToDoItemDTO> items;
}
