package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.responseDto.JwtResponse;
import ru.skypro.homework.security.JwtTokenUtil;
import ru.skypro.homework.service.AuthService;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "Authentication", description = "API для регистрации и входа пользователей")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Operation(
            summary = "Вход в систему",
            description = "Аутентифицирует пользователя и возвращает JWT токен.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Логин и пароль",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Login.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный вход", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Неверные учётные данные")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid Login login) {
        JwtResponse response = authService.login(login);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя. Логин должен быть уникальным.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для регистрации",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Register.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                    @ApiResponse(responseCode = "409", description = "Пользователь с таким логином уже существует")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid Register register) {
        authService.register(register);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
