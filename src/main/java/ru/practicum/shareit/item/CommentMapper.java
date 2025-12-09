package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(componentModel = "spring", uses = ItemMapper.class)
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    Comment mapToComment(CommentCreateDto dto, Item item, User author);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto mapToCommentDto(Comment comment);

    List<CommentDto> mapToCommentDtoList(List<Comment> comments);
}