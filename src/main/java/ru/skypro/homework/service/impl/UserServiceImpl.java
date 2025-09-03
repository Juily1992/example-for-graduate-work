package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.UserDto;
import ru.skypro.homework.service.UserService;

//Бизнес-логика: обновление профиля, фото, пароля и т.д.

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserDto getUserDto(User user) {
        // TODO: реализовать
        return null;
    }

    @Override
    public UserDto updateUser(User user, UpdateUser dto) {
        // TODO: реализовать
        return null;
    }

    @Override
    public String updateUserImage(User user, MultipartFile image) {
        // TODO: реализовать
        return null;
    }

    @Override
    public void setPassword(User user, String currentPassword, String newPassword) {
        // TODO: реализовать
    }
}