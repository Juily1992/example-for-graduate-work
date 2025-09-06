package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.UserDto;
import ru.skypro.homework.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Проверяют управление пользовательскими данными:
// (Получение профиля, Обновление данных, Смену пароля с проверкой старого пароля)

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void setPassword_CorrectCurrentPassword_ShouldUpdatePassword() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@mail.com");

        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPass");
        newPassword.setNewPassword("newPass");

        User user = new User();
        user.setPassword("encodedOldPass");

        when(userRepository.findByUsername("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        // Act
        userService.setPassword(newPassword, userDetails);

        // Assert
        verify(passwordEncoder, times(1)).matches("oldPass", "encodedOldPass");
        verify(passwordEncoder, times(1)).encode("newPass");
        assertEquals("encodedNewPass", user.getPassword());
    }

    @Test
    void setPassword_WrongCurrentPassword_ShouldThrowException() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@mail.com");

        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("wrongPass");
        newPassword.setNewPassword("newPass");

        User user = new User();
        user.setPassword("encodedOldPass");

        when(userRepository.findByUsername("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedOldPass")).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            userService.setPassword(newPassword, userDetails);
        });
    }
}