package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.comment.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentDto> comments;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
}