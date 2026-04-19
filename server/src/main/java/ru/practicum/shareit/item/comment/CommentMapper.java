package ru.practicum.shareit.item.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class CommentMapper {

    public static Comment mapToComment(CommentCreateDto dto, Item item, User author) {
        return Comment.builder()
                .text(dto.getText())
                .item(item)
                .author(author)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<CommentDto> mapToCommentDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();
    }
}