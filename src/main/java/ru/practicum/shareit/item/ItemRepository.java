package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAll();

    List<Item> findByUserId(long userId);

    Item save(Item item);

    Optional<Item> findById(long itemId);

    void deleteByUserIdAndItemId(long userId, long itemId);
}