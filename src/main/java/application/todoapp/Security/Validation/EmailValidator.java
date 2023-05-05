package application.todoapp.Security.Validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private Pattern pattern;
    public EmailValidator() {
        pattern = Pattern.compile("^(.+)@(.+)$");
    }
    public boolean matchEmail(String email){
        return pattern.matcher(email).matches();
    }
}
