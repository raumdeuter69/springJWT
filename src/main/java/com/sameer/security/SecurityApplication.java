package com.sameer.security;

import com.sameer.security.auth.AuthenticationService;
import com.sameer.security.auth.RegisterRequest;
import com.sameer.security.user.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService authService)
    {
        return args -> {
            var admin= RegisterRequest.builder()
                    .firstName("Admin")
                    .lastName("Admin")
                    .email("admin@admin.com")
                    .password("admin")
                    .role(Role.ADMIN)
                    .build();
            System.out.println(authService.register(admin).getToken()+  " " + ":" + "ADMIN TOKEN IS HERE");
        };
    }

}
