package ru.skypro.homework.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Проверяет работу эндпоинтов управления пользователями с аутентификацией и авторизацией.

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository; // добавляем доступ к репозиторию

    @Test
    @WithMockUser(username = "testuser@mail.com", roles = "USER")
    void getMe_AuthenticatedUser_ShouldReturnUserData() throws Exception {
        // Подготавливаем пользователя в базе перед запросом
        User user = new User();
        user.setUsername("testuser@mail.com"); // email
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password"); // пароль всё равно не проверяется в WithMockUser
        userRepository.save(user);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testuser@mail.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    void getMe_Unauthenticated_ShouldReturnUnauthorized() throws Exception {
        // Запрос без аутентификации
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }
}