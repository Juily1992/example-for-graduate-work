package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;


//загружает пользователя для Spring Security

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 Поиск пользователя: " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println(" Найден в БД: " + user.getUsername());
        System.out.println(" Хэш пароля из БД: " + user.getPassword());
        System.out.println(" Проверяю через PasswordEncoder: " + passwordEncoder.getClass().getSimpleName());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
