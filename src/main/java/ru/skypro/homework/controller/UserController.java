package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.UserDto;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "API для управления данными пользователя: просмотр и редактирование профиля")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Получение своего профиля",
            description = "Возвращает данные текущего пользователя: имя, фамилия, телефон."
    )
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getUserDto(user));
    }

    @Operation(
            summary = "Обновление профиля",
            description = "Изменяет имя, фамилию и телефон текущего пользователя."
    )
    @PatchMapping("/me")
    public ResponseEntity<UserDto> updateUser(
            @RequestBody @Valid UpdateUser dto,
            @AuthenticationPrincipal User user) {
        UserDto updated = userService.updateUser(user, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Обновление аватара",
            description = "Заменяет аватар текущего пользователя. Файл должен быть изображением.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Файл изображения (JPG, PNG)",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "object"),
                            encoding = {
                                    @Encoding(name = "image", contentType = "image/jpeg, image/png")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Аватар успешно обновлён"),
                    @ApiResponse(responseCode = "400", description = "Файл пустой или не изображение"),
                    @ApiResponse(responseCode = "401", description = "Не авторизован"),
                    @ApiResponse(responseCode = "413", description = "Файл слишком большой")
            }
    )
    @PatchMapping("/me/image")
    public ResponseEntity<?> updateUserImage(
            @RequestPart("image") MultipartFile image,
            @AuthenticationPrincipal User user) {
        String imagePath = userService.updateUserImage(user, image);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Смена пароля",
            description = "Позволяет пользователю изменить свой пароль. Требуется текущий пароль.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Текущий и новый пароль",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NewPassword.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пароль успешно изменён"),
                    @ApiResponse(responseCode = "400", description = "Неверный текущий пароль или слабый новый"),
                    @ApiResponse(responseCode = "401", description = "Не авторизован")
            }
    )
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(
            @RequestBody @Valid NewPassword dto,
            @AuthenticationPrincipal User user) {
        userService.setPassword(user, dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.ok().build();
    }
}