package application.todoapp.Security.Authorization.Request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record CreateUserRequest(@NotNull @NotEmpty String email,@NotNull @NotEmpty  String password) {
}
