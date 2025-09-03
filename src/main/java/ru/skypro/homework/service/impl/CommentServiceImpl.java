package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.CommentsResponse;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    public CommentDto addComment(Long adId, String username, CreateOrUpdateComment dto) {
        log.info("Добавление комментария к объявлению ID: {} от пользователя: {}", adId, username);

        // 1. Находим объявление
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> {
                    log.warn("Объявление не найдено: ID={}", adId);
                    return new EntityNotFoundException("Ad not found");
                });

        // 2. Находим автора
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: {}", username);
                    return new EntityNotFoundException("User not found");
                });

        // 3. Создаём комментарий
        Comment comment = new Comment();
        comment.setAd(ad);
        comment.setAuthor(author);
        comment.setText(dto.getText());
        comment.setCreatedAt(LocalDateTime.now());

        // 4. Сохраняем
        Comment saved = commentRepository.save(comment);

        // 5. Используем MapStruct для маппинга
        CommentDto result = CommentMapper.INSTANCE.toCommentDto(saved);

        log.info("Комментарий успешно добавлен, ID: {}", result.getPk());
        return result;
    }

    @Override
    public void deleteComment(Long adId, Long commentId) {
        Comment comment = commentRepository.findByAdIdAndId(commentId, adId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        commentRepository.delete(comment);
    }

    @Override
    public CommentDto updateComment(Long adId, Long commentId, CreateOrUpdateComment dto) {
        Comment comment = commentRepository.findByAdIdAndId(adId, commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        comment.setText(dto.getText());
        Comment updated = commentRepository.save(comment);
        return CommentMapper.INSTANCE.toCommentDto(updated);
    }

    @Override
    public CommentsResponse getComments(Long adId) {
        List<Comment> comments = commentRepository.findByAdId(adId);
        List<CommentDto> dtos = CommentMapper.INSTANCE.toCommentDtoList(comments);
        return new CommentsResponse(dtos.size(), dtos);
    }
    }