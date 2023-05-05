package application.todoapp.Security.Authorization;

import application.todoapp.Security.Authorization.Request.AuthenticateUserRequest;
import application.todoapp.Security.Authorization.Request.CreateUserRequest;
import application.todoapp.Security.Authorization.Response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @PostMapping("/create")
    public ResponseEntity<AuthenticationResponse> createUser(@RequestBody CreateUserRequest request){
        return new ResponseEntity<>(authorizationService.createUser(request), HttpStatusCode.valueOf(201));
    }
    @PostMapping("/authorize")
    public ResponseEntity<AuthenticationResponse> authorizeUser(@RequestBody AuthenticateUserRequest request){
        return new ResponseEntity<>(authorizationService.authenticate(request), HttpStatusCode.valueOf(200));
    }
}
