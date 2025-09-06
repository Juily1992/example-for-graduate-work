package ru.skypro.homework.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Проверяет работу эндпоинтов регистрации в реальной среде с подключением к БД и полным стеком Spring.

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    //Очистка базы данных перед каждым тестом

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void register_ValidData_ShouldReturnCreated() throws Exception {
        Register register = new Register();
        register.setUsername("newuser@test.com");
        register.setPassword("password123");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+7 (123) 456-78-90");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());
    }

    @Test
    void register_DuplicateUsername_ShouldReturnConflict() throws Exception {
        Register firstUser = new Register();
        firstUser.setUsername("duplicate@test.com");
        firstUser.setPassword("password123");
        firstUser.setFirstName("First");
        firstUser.setLastName("User");
        firstUser.setPhone("+7 (111) 222-33-44");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)))
                .andExpect(status().isCreated());

        Register duplicateUser = new Register();
        duplicateUser.setUsername("duplicate@test.com");
        duplicateUser.setPassword("newpassword456");
        duplicateUser.setFirstName("Second");
        duplicateUser.setLastName("User");
        duplicateUser.setPhone("+7 (555) 666-77-88");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isConflict()); // проверяем только 409
    }

    @Test
    void register_InvalidPassword_ShouldReturnBadRequest() throws Exception {
        // Тест на короткий пароль
        Register register = new Register();
        register.setUsername("test@mail.com");
        register.setPassword("short"); // 5 символов - не проходит валидацию
        register.setFirstName("Test");
        register.setLastName("User");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void register_InvalidPhone_ShouldReturnBadRequest() throws Exception {
        // Тест на невалидный телефон
        Register register = new Register();
        register.setUsername("test@mail.com");
        register.setPassword("validpassword123");
        register.setFirstName("Test");
        register.setLastName("User");
        register.setPhone("invalid-phone"); // Невалидный формат

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.phone").exists());
    }

    @Test
    void register_MissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Тест на отсутствие обязательных полей
        Register register = new Register();
        register.setUsername("test@mail.com");
        // password отсутствует - обязательно
        register.setFirstName("Test");
        register.setLastName("User");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void register_ValidWithoutPhone_ShouldReturnCreated() throws Exception {
        // Тест на регистрацию без телефона (phone не обязателен)
        Register register = new Register();
        register.setUsername("nophone@test.com");
        register.setPassword("password123");
        register.setFirstName("No");
        register.setLastName("Phone");
        // phone не устанавливаем - должно работать

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated());
    }
}