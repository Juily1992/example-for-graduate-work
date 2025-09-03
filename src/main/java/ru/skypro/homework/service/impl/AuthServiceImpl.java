package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.model.User;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.JwtResponse;
import ru.skypro.homework.security.JwtTokenUtil;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public JwtResponse login(Login login) {
        log.info("Попытка входа: {}", login.getUsername());

        try {
            // Аутентифицируем пользователя
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);

            log.info("Успешная аутентификация, токен сгенерирован для: {}", userDetails.getUsername());
            return new JwtResponse(token);

        } catch (BadCredentialsException e) {
            log.warn("Неверные учётные данные для пользователя: {}", login.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        } catch (DisabledException e) {
            log.warn("Попытка входа отключённого пользователя: {}", login.getUsername());
            throw new DisabledException("User is disabled");
        } catch (Exception e) {
            log.error("Ошибка аутентификации: {}", e.getMessage(), e);
            throw new RuntimeException("Authentication failed", e);
        }
    }
    @Override
    public boolean login(String username, String password) {
        System.out.println("PasswordEncoder: " + passwordEncoder);
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Override
    public void register(Register register) {
        if (userRepository.findByUsername(register.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = UserMapper.INSTANCE.toUser(register);

        // 2. Хэшируем пароль
        user.setPassword(passwordEncoder.encode(register.getPassword()));

        // 3. Сохраняем в БД
        userRepository.save(user);
    }
}
