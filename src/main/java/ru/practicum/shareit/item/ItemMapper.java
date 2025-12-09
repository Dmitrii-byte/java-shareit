package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
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

@UtilityClass
public class ItemMapper {

    public static Item mapToItem(CreateItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemResponseDto mapToItemResponseDto(Item item, LocalDateTime last,
                                                       LocalDateTime next, List<CommentDto> comments) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                comments,
                last,
                next
        );
    }

    public static List<ItemResponseDto> mapToItemResponseDto(List<Item> items, Map<Long, List<CommentDto>> commentsMap) {
        return items.stream()
                .map(item -> mapToItemResponseDto(
                        item,
                        null,
                        null,
                        commentsMap.getOrDefault(item.getId(), List.of())
                ))
                .toList();
    }

    public static ItemResponseDto mapToItemResponseDto(Item item, List<CommentDto> comments) {
        return mapToItemResponseDto(item, null, null, comments);
    }
}