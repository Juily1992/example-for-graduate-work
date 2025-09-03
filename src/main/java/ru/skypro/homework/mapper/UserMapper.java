package ru.skypro.homework.mapper;


import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.UserDto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Маппер для преобразования DTO в сущность User и обратно.
 * Использует @ObjectFactory для корректной инициализации User при использовании @Builder.
 */
@Mapper(
        imports = {Role.class, LocalDateTime.class}
)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Преобразует Register в User.
     * Основные поля маппятся автоматически.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", expression = "java(Role.USER)")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    User toUser(Register register);

    /**
     * Обновляет существующего пользователя.
     * Не перезаписывает null-поля.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUser dto, @MappingTarget User user);

    /**
     * Преобразует User в UserDto.
     * username используется как email.
     */
    @Mapping(source = "username", target = "email")
    UserDto toUserDto(User user);

    /**
     * Выполняется ПОСЛЕ маппинга Register → User.
     * Добавляет дефолтные значения и валидацию.
     */
    @AfterMapping
    default void afterToUser(Register register, @MappingTarget User user) {
        // Валидация: username обязателен
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username must not be null or empty");
        }

        // Дефолты, если поля null
        if (user.getFirstName() == null) {
            user.setFirstName("");
        }

        if (user.getLastName() == null) {
            user.setLastName("");
        }

        if (user.getPhone() == null) {
            user.setPhone("");
        }

        // Устанавливаем дефолтные значения
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        // Логирование (в реальном проекте используйте Logger)
        System.out.println("Пользователь создан: " + user.getUsername());
    }

    /**
     * Выполняется ПОСЛЕ обновления пользователя.
     * Проверяет и очищает поля.
     */
    @AfterMapping
    default void afterUpdateUser(UpdateUser dto, @MappingTarget User user) {
        // Если в DTO переданы пустые строки — заменяем на пустую строку
        if (dto.getFirstName() != null && dto.getFirstName().isBlank()) {
            user.setFirstName("");
        }

        if (dto.getLastName() != null && dto.getLastName().isBlank()) {
            user.setLastName("");
        }

        if (dto.getPhone() != null && dto.getPhone().isBlank()) {
            user.setPhone("");
        }

        // Логирование
        System.out.println("Профиль обновлён: " + user.getUsername());
    }
}