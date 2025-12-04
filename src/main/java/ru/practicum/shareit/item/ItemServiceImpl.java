package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getItems(long userId) {
        return ItemMapper.mapToItemDto(repository.findByUserId(userId));
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        if (userRepository.getUserById(userId).isPresent()) {
            return ItemMapper.mapToItemDto(repository.save(ItemMapper.mapToItem(itemDto, userId)));
        } else {
            throw new NotFoundException("Пользователя с id " + userId + " не найден, создать предмет не получилось");
        }
    }

    @Override
    public ItemDto getItemById(long itemId) {
        if (repository.findById(itemId).isPresent()) {
            return ItemMapper.mapToItemDto(repository.findById(itemId).get());
        } else {
            throw new NotFoundException("Предмет с id " + itemId + " не найден");
        }
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDtoUpdate itemUpdate) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        if (!item.getOwner().equals(userId)) {
            throw new NotFoundException("Вы не владелец предмета с id " + itemId);
        }
        if (itemUpdate.getName() != null) {
            item.setName(itemUpdate.getName());
        }
        if (itemUpdate.getDescription() != null) {
            item.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getAvailable() != null) {
            item.setAvailable(itemUpdate.getAvailable());
        }
        repository.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> search(String name) {
        if (name.isBlank()) {
            return List.of();
        }
        String lowerCase = name.toLowerCase();
        return repository.getAll().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName() != null && item.getName().toLowerCase().contains(lowerCase))
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        if (!item.getOwner().equals(userId)) {
            throw new ValidationException("Вы не владелец предмета с id " + itemId);
        }
        repository.deleteByUserIdAndItemId(userId, itemId);
    }
}