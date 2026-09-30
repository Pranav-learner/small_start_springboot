package com.example.demo.controller;

import com.example.demo.dto.AdminCreateUserRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldDenyUnauthenticatedUserFromCreatingAdmin() throws Exception {
        AdminCreateUserRequest request = new AdminCreateUserRequest(
                "New Admin",
                "newadmin@example.com",
                "password123",
                "ROLE_ADMIN"
        );

        mockMvc.perform(post("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldDenyRegularUserFromCreatingAdmin() throws Exception {
        User regularUser = new User(
                "Regular User",
                "user@example.com",
                passwordEncoder.encode("password123"),
                "ROLE_USER"
        );
        userRepository.save(regularUser);

        String userToken = jwtService.generateToken(Map.of("role", "ROLE_USER"), regularUser.getEmail());

        AdminCreateUserRequest request = new AdminCreateUserRequest(
                "Another Admin",
                "anotheradmin@example.com",
                "password123",
                "ROLE_ADMIN"
        );

        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminToCreateNewAdmin() throws Exception {
        User adminUser = new User(
                "Admin User",
                "admin@example.com",
                passwordEncoder.encode("admin123"),
                "ROLE_ADMIN"
        );
        userRepository.save(adminUser);

        String adminToken = jwtService.generateToken(Map.of("role", "ROLE_ADMIN"), adminUser.getEmail());

        AdminCreateUserRequest request = new AdminCreateUserRequest(
                "Second Admin",
                "admin2@example.com",
                "password123",
                "ROLE_ADMIN"
        );

        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("admin2@example.com"))
                .andExpect(jsonPath("$.name").value("Second Admin"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }
}
