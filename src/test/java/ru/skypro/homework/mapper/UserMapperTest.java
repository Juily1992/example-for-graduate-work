package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;

import static org.junit.jupiter.api.Assertions.*;

// Проверяют преобразование между DTO и сущностью User, включая регистрацию и обновление данных пользователя.

class UserMapperTest {

    @Test
    void toUser_FromRegister_ShouldMapCorrectly() {
        // Arrange
        Register register = new Register();
        register.setUsername("test@mail.com");
        register.setPassword("password123");
        register.setFirstName("John");
        register.setLastName("Doe");
        register.setPhone("+7 (123) 456-78-90");

        // Act
        User user = UserMapper.INSTANCE.toUser(register);

        // Assert
        assertNull(user.getId()); // ID должен быть null для новой сущности
        assertEquals("test@mail.com", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("+7 (123) 456-78-90", user.getPhone());
        assertNotNull(user.getCreatedAt()); // Дата должна проставляться автоматически
    }

    @Test
    void updateUserFromDto_ShouldIgnoreNullValues() {
        // Проверка частичного обновления (только указанные поля)
        User existingUser = new User();
        existingUser.setFirstName("OldName");
        existingUser.setLastName("OldLastName");
        existingUser.setPhone("+7 (000) 000-00-00");

        UpdateUser updateDto = new UpdateUser();
        updateDto.setFirstName("NewName"); // Меняем только имя

        // Act
        UserMapper.INSTANCE.updateUserFromDto(updateDto, existingUser);

        // Assert
        assertEquals("NewName", existingUser.getFirstName()); // Обновлено
        assertEquals("OldLastName", existingUser.getLastName()); // Осталось прежним
        assertEquals("+7 (000) 000-00-00", existingUser.getPhone()); // Осталось прежним
    }
}
