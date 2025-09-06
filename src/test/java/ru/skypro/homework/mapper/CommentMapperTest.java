package ru.skypro.homework.mapper;

import org.junit.jupiter.api.Test;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.responseDto.CommentDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// Проверяют корректность преобразования сущности Comment в CommentDto и обратно. Особое внимание - маппингу временных меток и путей к изображениям.

class CommentMapperTest {

    @Test
    void toCommentDto_ShouldMapCorrectly() {
        // Arrange - подготовка данных
        User user = new User();
        user.setId(1L);
        user.setFirstName("Ivan");
        user.setImage("avatar.jpg");

        Comment comment = new Comment();
        comment.setId(100L);
        comment.setAuthor(user);
        comment.setText("Test comment");
        comment.setCreatedAt(LocalDateTime.of(2023, 1, 1, 12, 0));

        // Act - выполнение действия
        CommentDto dto = CommentMapper.INSTANCE.toCommentDto(comment);

        // Assert - проверка результатов
        assertEquals(100, dto.getPk());
        assertEquals(1, dto.getAuthor());
        assertEquals("Ivan", dto.getAuthorFirstName());
        assertEquals("/images/users/avatar.jpg", dto.getAuthorImage());
        assertEquals("Test comment", dto.getText());
        assertNotNull(dto.getCreatedAt());
    }

    @Test
    void toCommentDto_ShouldHandleNullImage() {
        // Проверка обработки отсутствующего изображения
        User user = new User();
        user.setImage(null);

        Comment comment = new Comment();
        comment.setAuthor(user);

        CommentDto dto = CommentMapper.INSTANCE.toCommentDto(comment);

        assertEquals("/images/users/default.jpg", dto.getAuthorImage());
    }
}