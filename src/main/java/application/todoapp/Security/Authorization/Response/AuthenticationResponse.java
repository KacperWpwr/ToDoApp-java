package application.todoapp.Security.Authorization.Response;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
}
