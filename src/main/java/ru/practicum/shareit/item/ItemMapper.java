package ru.practicum.shareit.item;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface ItemMapper {
    Item mapToItem(CreateItemDto itemDto);

    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "lastBooking", source = "last")
    @Mapping(target = "nextBooking", source = "next")
    ItemResponseDto mapToItemResponseDto(Item item, LocalDateTime last,
                                         LocalDateTime next, List<CommentDto> comments);

    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    ItemResponseDto mapToItemResponseDto(Item item, List<CommentDto> comments);

    List<ItemResponseDto> mapToItemResponseDtoList(List<Item> items,
                                                   @Context Map<Long, List<CommentDto>> commentsMap);
}