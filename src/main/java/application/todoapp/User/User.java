package application.todoapp.User;

import application.todoapp.ToDoItem.DTO.ToDoItemDTO;

import application.todoapp.ToDoItem.ToDoItem;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "AppUser")
@Getter@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User  implements UserDetails {
    @Id
    private String email;

    private String password;


    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch= FetchType.LAZY)
    @Builder.Default
    private Set<ToDoItem> todos = new HashSet<>();

    public List<ToDoItemDTO> addItem(ToDoItem item){
        todos.add(item);

        return todos.stream()
                .map(ToDoItem::getDTO).toList();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
