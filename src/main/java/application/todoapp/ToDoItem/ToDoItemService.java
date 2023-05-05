package application.todoapp.ToDoItem;

import application.todoapp.ToDoItem.DTO.ToDoItemDTO;
import application.todoapp.ToDoItem.DTO.ToDoListDTO;
import application.todoapp.ToDoItem.Exception.ToDoItemNotFoundException;
import application.todoapp.ToDoItem.Request.CreateToDoRequest;
import application.todoapp.ToDoItem.Request.EditToDoRequest;
import application.todoapp.User.User;
import application.todoapp.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ToDoItemService {
    private final ToDoItemRepository itemRepository;
    private final UserService userService;

    public ToDoListDTO create(CreateToDoRequest request) {
        User user = userService.getUser(request.user_email());
        ToDoItem item = ToDoItem.builder().title(request.name())
                .description(request.description())
                .date(request.date())
                .user(user)
                .build();
        item = itemRepository.save(item);

        List<ToDoItemDTO> items = user.addItem(item);

        userService.saveUser(user);

        return new ToDoListDTO(items);

    }

    public ToDoListDTO edit(EditToDoRequest request) {
        ToDoItem item = itemRepository.findById(request.id()).orElseThrow(ToDoItemNotFoundException::new);

        if(request.name() != null){
            item.setTitle(request.name());
        }

        if(request.description() != null){
            item.setDescription(request.description());
        }

        if(request.date() != null){
            item.setDate(request.date());
        }
        item = itemRepository.save(item);

        return new ToDoListDTO(item.getUser().getTodos()
                .stream().map(ToDoItem::getDTO).toList());
    }

    public ToDoListDTO delete(Long id) {
        ToDoItem item = itemRepository.findById(id).orElseThrow(ToDoItemNotFoundException::new);
        itemRepository.delete(item);
        itemRepository.flush();
        User user = userService.getUser(item.getUser().getEmail());
        return new ToDoListDTO(user.getTodos().stream().map(ToDoItem::getDTO).toList());
    }
}
