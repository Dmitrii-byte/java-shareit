package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItems(long userId);

    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemDto getItemById(long itemId);

    ItemDto updateItem(long userId, long itemId, ItemDtoUpdate itemUpdate);

    List<ItemDto> search(String name);

    void deleteItem(long userId, long itemId);
}