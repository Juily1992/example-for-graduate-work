package ru.skypro.homework.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.UserDto;

public interface UserService {
    UserDto getUserDto(UserDetails userDetails);

    UserDto updateUser(UpdateUser dto, UserDetails userDetails);

    String updateUserImage(MultipartFile image, UserDetails userDetails);

    void setPassword(NewPassword dto, UserDetails userDetails);
}
