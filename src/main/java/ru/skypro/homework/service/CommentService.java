package ru.skypro.homework.service;


import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.responseDto.CommentDto;
import ru.skypro.homework.responseDto.CommentsResponse;

public interface CommentService {
    CommentDto addComment(Long adId, String username, CreateOrUpdateComment dto);

    void deleteComment(Long adId, Long commentId);

    CommentDto updateComment(Long adId, Long commentId, CreateOrUpdateComment dto);

    CommentsResponse getComments(Long adId);
}