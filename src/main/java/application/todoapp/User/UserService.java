package application.todoapp.User;

import application.todoapp.Security.Authorization.Request.CreateUserRequest;
import application.todoapp.Security.Validation.EmailValidator;
import application.todoapp.ToDoItem.DTO.ToDoListDTO;
import application.todoapp.ToDoItem.ToDoItem;
import application.todoapp.User.Exceptions.EmailNotValidException;
import application.todoapp.User.Exceptions.EmailTakenException;
import application.todoapp.User.Exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator;

    public User createUser(User user){

        if(userRepository.existsById(user.getEmail())){
            throw new EmailTakenException();
        }
        if(!emailValidator.matchEmail((user.getEmail()))){
            throw new EmailNotValidException();
        }


         return userRepository.save(user);
    }

    public User getUser(String email){

        return userRepository.findById(email).orElseThrow(UserNotFoundException::new);
    }

    public UserDetails loadUserByUsername(String username) {
        return getUser(username);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public ToDoListDTO getUserToDos(String email) {
        User user = getUser(email);

        return new ToDoListDTO(user.getTodos()
                .stream().map(ToDoItem::getDTO).toList());
    }
}
