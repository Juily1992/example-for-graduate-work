package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.UserDto;
import ru.skypro.homework.service.UserService;

//Бизнес-логика: обновление профиля, фото, пароля и т.д.

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Override
    public UserDto getUserDto(UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.INSTANCE.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UpdateUser dto, UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserMapper.INSTANCE.updateUserFromDto(dto, user);
        User saved = userRepository.save(user);
        return UserMapper.INSTANCE.toUserDto(saved);
    }

    @Override
    public String updateUserImage(MultipartFile image, UserDetails userDetails) {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty");
        }

        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            String filename = imageService.saveImage(image, "users");
            user.setImage(filename);
            userRepository.save(user);
            return "/images/users/" + filename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    @Override
    public void setPassword(NewPassword dto, UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        if (dto.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}