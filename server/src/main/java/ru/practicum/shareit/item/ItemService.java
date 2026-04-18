package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentCreateDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    List<ItemResponseDto> getItemsByUserId(long userId);

    ItemResponseDto addNewItem(long userId, CreateItemDto itemDto);

    ItemResponseDto getItemById(long itemId, long userId);

    ItemResponseDto updateItem(long userId, long itemId, ItemUpdateDto itemUpdate);

    List<ItemResponseDto> search(String text);

    CommentDto addComment(long userId, long itemId, CommentCreateDto commentCreateDto);

    void deleteItem(long userId, long itemId);
}