package ru.skypro.homework.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.service.impl.CommentServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


// Проверяют бизнес-логику работы с комментариями:
// (Добавление комментария, Удаление комментария, Проверку прав доступа)
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void addComment_ValidData_ShouldReturnCommentDto() {
        // Входные данные
        Long adId = 1L;
        String username = "test@mail.com";

        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText("Great ad!");

        Ad ad = new Ad();
        ad.setId(adId);

        User author = new User();
        author.setId(1L);
        author.setUsername(username);

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(author));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setId(100L);
            return comment;
        });

        // Вызов метода, который тестирует
        CommentDto result = commentService.addComment(adId, username, dto);

        // Проверка, что делает метод
        assertNotNull(result);
        assertEquals(100, result.getPk());
        assertEquals("Great ad!", result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}