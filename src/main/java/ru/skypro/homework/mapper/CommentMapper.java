package ru.skypro.homework.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.responseDto.CommentDto;

import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    /**
     * Преобразует сущность Comment в CommentDto.
     * Обратите внимание:
     * - id → pk
     * - author.id → author
     * - author.firstName → authorFirstName
     * - author.image → authorImage (с префиксом)
     * - createdAt → toEpochSecond в UTC
     */
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "author.id", target = "author")
    @Mapping(source = "author.firstName", target = "authorFirstName")
    @Mapping(source = "author.image", target = "authorImage", qualifiedByName = "addImagePrefix")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(source = "text", target = "text")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDtoList(List<Comment> comments);

    @AfterMapping
    default void mapCreatedAt(Comment comment, @MappingTarget CommentDto dto) {
        dto.setCreatedAt(comment.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
    }

    @Named("addImagePrefix")
    default String addImagePrefix(String image) {
        if (image == null || image.isBlank()) {
            return "/images/users/default.jpg";
        }
        return "/images/users/" + image;
    }
}