package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest, List<ItemRequestDto.Item> items) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setCreated(itemRequest.getCreated());
        dto.setItems(items != null ? items : List.of());
        return dto;
    }

    public static ItemRequestDto.Item mapToItemForRequest(Item item) {
        ItemRequestDto.Item itemDto = new ItemRequestDto.Item();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setOwnerId(item.getOwner().getId());
        return itemDto;
    }
}
