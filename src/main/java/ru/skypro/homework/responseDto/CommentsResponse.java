package ru.skypro.homework.responseDto;

// описывает структуру ответа при получении комментариев к объявлению

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommentsResponse {
    private Integer count;
    private List<CommentDto> results;
}
