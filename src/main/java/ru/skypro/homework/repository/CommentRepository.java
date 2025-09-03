package ru.skypro.homework.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Найти все комментарии по ID объявления

    List<Comment> findByAdId(Long adId);

    Optional<Comment> findByAdIdAndId(Long adId, Long id);
}
