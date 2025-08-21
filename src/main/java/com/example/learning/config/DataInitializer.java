
package com.example.learning.config;

import com.example.learning.entity.Role;
import com.example.learning.entity.User;
import com.example.learning.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner seedAdmin(UserRepository userRepository, PasswordEncoder encoder) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                if (userRepository.count() == 0) {
                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setPassword(encoder.encode("admin123"));
                    admin.setFullName("System Administrator");
                    admin.setRoles(Collections.singleton(Role.ADMIN));
                    userRepository.save(admin);
                    System.out.println(">>> Default admin created: username=admin password=admin123");
                }
            }
        };
    }
}
