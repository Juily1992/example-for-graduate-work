package ru.skypro.homework.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;


import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    // Найти все объявления по автору
    List<Ad> findByAuthor(User author);

    // Найти все объявления по ID автора (удобно для /ads/me)
    List<Ad> findByAuthorId(Long authorId);
}