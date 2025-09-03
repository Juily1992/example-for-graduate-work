package ru.skypro.homework.model;

//модель пользователя, писывает, как выглядит пользователь в системе и как он хранится в таблице users базы данных

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skypro.homework.dto.Role;

@Entity
@Builder
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String firstName;
    private String lastName;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String image; // путь к аватарке


}
