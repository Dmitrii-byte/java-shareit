package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items;

    @Override
    public List<Item> getAll() {
        return items.values().stream().toList();
    }

    @Override
    public List<Item> findByUserId(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == userId)
                .toList();
    }

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(getIdForItem());
        }
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        items.remove(itemId);

    }

    private long getIdForItem() {
        long id = items.values().stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        return ++id;
    }
}