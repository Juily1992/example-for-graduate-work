package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exceptions.UsernameExistsException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public void register(Register register) {
        if (userRepository.findByUsername(register.getUsername()).isPresent()) {
            throw new UsernameExistsException("User with this username already exists");
        }

        User user = UserMapper.INSTANCE.toUser(register);
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        userRepository.save(user);
    }
}