package application.todoapp.Security.Authorization.Request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record AuthenticateUserRequest(@NotNull @NotEmpty String email, @NotNull @NotEmpty String password) {

}
