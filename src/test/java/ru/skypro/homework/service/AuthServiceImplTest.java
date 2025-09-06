package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exceptions.UsernameExistsException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Проверяют логику регистрации пользователей (Успешную регистрацию нового пользователя, Обработку случая, когда пользователь уже существует, Шифрование пароля)

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_NewUser_ShouldSaveEncryptedPassword() {
        // Arrange
        Register register = new Register();
        register.setUsername("newuser@mail.com");
        register.setPassword("plainPassword");
        register.setFirstName("Test");
        register.setLastName("User");

        when(userRepository.findByUsername("newuser@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        authService.register(register);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("plainPassword");
    }

    @Test
    void register_ExistingUser_ShouldThrowException() {
        // Arrange
        Register register = new Register();
        register.setUsername("existing@mail.com");

        User existingUser = new User();
        when(userRepository.findByUsername("existing@mail.com")).thenReturn(Optional.of(existingUser));

        // Act и Assert
        assertThrows(UsernameExistsException.class, () -> {
            authService.register(register);
        });
    }
}