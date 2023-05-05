package application.todoapp.ToDoItem;

import application.todoapp.ToDoItem.DTO.ToDoItemDTO;
import application.todoapp.User.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToDoItem {
    @SequenceGenerator(
            name = "item_sequence",
            sequenceName = "item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "item_sequence",
            strategy = GenerationType.SEQUENCE
    )
    @Id
    private Long id;
    private String title;
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date date;

    @ManyToOne
    private User user;


    public ToDoItemDTO getDTO(){
        return new ToDoItemDTO(id,title,description,date);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToDoItem toDoItem = (ToDoItem) o;
        return Objects.equals(id, toDoItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
