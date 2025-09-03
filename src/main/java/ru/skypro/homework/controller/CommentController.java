package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.CommentsResponse;
import ru.skypro.homework.service.CommentService;

@RestController
@RequestMapping("/ads/{adId}/comments")
@Tag(name = "Comments", description = "API для работы с комментариями к объявлениям")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    @Operation(
            summary = "Получение комментариев к объявлению",
            description = "Возвращает все комментарии к объявлению с пагинацией."
    )
    @GetMapping
    public ResponseEntity<CommentsResponse> getComments(@PathVariable Long adId) {
        CommentsResponse response = commentService.getComments(adId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Добавление комментария",
            description = "Оставляет комментарий под объявлением. Автор — текущий пользователь."
    )
    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long adId,
            @RequestBody @Valid CreateOrUpdateComment commentDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CommentDto dto = commentService.addComment(adId, userDetails.getUsername(), commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Удаление комментария",
            description = "Удаляет комментарий. Только автор или администратор может удалить."
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long adId,
            @PathVariable Long commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Редактирование комментария",
            description = "Меняет текст комментария. Только автор может редактировать."
    )
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long adId,
            @PathVariable Long commentId,
            @RequestBody @Valid CreateOrUpdateComment dto) {
        CommentDto updated = commentService.updateComment(adId, commentId, dto);
        return ResponseEntity.ok(updated);
    }
}
