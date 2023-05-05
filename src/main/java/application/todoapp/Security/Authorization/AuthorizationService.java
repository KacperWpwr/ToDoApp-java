package application.todoapp.Security.Authorization;

import application.todoapp.Security.Authorization.Request.AuthenticateUserRequest;
import application.todoapp.Security.Authorization.Request.CreateUserRequest;
import application.todoapp.Security.Authorization.Response.AuthenticationResponse;
import application.todoapp.Security.JWT.JwtService;
import application.todoapp.User.User;
import application.todoapp.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authentication_manager;
    private final PasswordEncoder encoder;



    public AuthenticationResponse createUser(CreateUserRequest request) {
        User new_user = User.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))
                .build();
         new_user = userService.createUser(new_user);

        return new AuthenticationResponse(jwtService.generateToken(new_user));
    }

    public AuthenticationResponse authenticate(AuthenticateUserRequest request){
        authentication_manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userService.getUser(request.email());


        return new AuthenticationResponse( jwtService.generateToken(user));
    }
}
