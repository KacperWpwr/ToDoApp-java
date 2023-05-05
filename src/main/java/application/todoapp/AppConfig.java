package application.todoapp;

import application.todoapp.Security.Validation.EmailValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EmailValidator getEmailVAlidator(){
        return new EmailValidator();
    }

}
