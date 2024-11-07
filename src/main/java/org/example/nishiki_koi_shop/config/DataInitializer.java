package org.example.nishiki_koi_shop.config;

import org.example.nishiki_koi_shop.model.entity.*;
import org.example.nishiki_koi_shop.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("lam1782004@gmail.com").isEmpty()){
                Role userRole = roleRepository.findByName("ROLE_CUSTOMER")
                        .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_CUSTOMER").build()));

                User user = User.builder()
                        .username("Nguyen Lam")
                        .email("lam1782004@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Nguyen Thanh Lam")
                        .role(userRole)
                        .createdDate(LocalDate.now())
                        .address("42/1")
                        .phoneNumber("0362651806")
                        .build();

                userRepository.save(user);
            }

            if (userRepository.findByEmail("manager@example.com").isEmpty()) {
                Role managerRole = roleRepository.findByName("ROLE_MANAGER")
                        .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_MANAGER").build()));

                User admin = User.builder()
                        .username("manager")
                        .email("manager@example.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Manager User")
                        .role(managerRole)
                        .createdDate(LocalDate.now())
                        .address("42/1")
                        .phoneNumber("0362651806")
                        .build();

                userRepository.save(admin);
            }

            // Create Sale Staff User
            if (userRepository.findByEmail("salestaff@example.com").isEmpty()) {
                Role saleStaffRole = roleRepository.findByName("ROLE_SALE_STAFF")
                        .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_SALE_STAFF").build()));

                User saleStaff = User.builder()
                        .username("sale_staff")
                        .email("salestaff@example.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Sale Staff User")
                        .role(saleStaffRole)
                        .createdDate(LocalDate.now())
                        .address("43/1")
                        .phoneNumber("0362651807")
                        .build();

                userRepository.save(saleStaff);
            }

            // Create Delivery Staff User
            if (userRepository.findByEmail("deliverystaff@example.com").isEmpty()) {
                Role deliveryStaffRole = roleRepository.findByName("ROLE_DELIVERY_STAFF")
                        .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_DELIVERY_STAFF").build()));

                User deliveryStaff = User.builder()
                        .username("delivery_staff")
                        .email("deliverystaff@example.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Delivery Staff User")
                        .role(deliveryStaffRole)
                        .createdDate(LocalDate.now())
                        .address("44/1")
                        .phoneNumber("0362651808")
                        .build();

                userRepository.save(deliveryStaff);
            }

            // Create Consulting Staff User
            if (userRepository.findByEmail("consultingstaff@example.com").isEmpty()) {
                Role consultingStaffRole = roleRepository.findByName("ROLE_CONSULTING_STAFF")
                        .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_CONSULTING_STAFF").build()));

                User consultingStaff = User.builder()
                        .username("consulting_staff")
                        .email("consultingstaff@example.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Consulting Staff User")
                        .role(consultingStaffRole)
                        .createdDate(LocalDate.now())
                        .address("45/1")
                        .phoneNumber("0362651809")
                        .build();

                userRepository.save(consultingStaff);
            }
        };
    }
}
    

