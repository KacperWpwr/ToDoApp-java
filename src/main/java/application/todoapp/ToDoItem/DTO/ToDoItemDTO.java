package application.todoapp.ToDoItem.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToDoItemDTO {
    @NotNull @NotEmpty
    private Long id;
    @NotNull @NotEmpty
    private String name;
    @NotNull
    private String description;
    @NotNull @NotEmpty @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;
}
