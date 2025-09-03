package ru.skypro.homework.service;


import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.UserDto;

public interface UserService {
    UserDto getUserDto(User user);

    UserDto updateUser(User user, UpdateUser dto);

    String updateUserImage(User user, MultipartFile image);

    void setPassword(User user, String currentPassword, String newPassword);
}
