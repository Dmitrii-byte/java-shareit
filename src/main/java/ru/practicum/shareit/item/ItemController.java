package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id")
                             @Positive(message = "id должен быть положительным") long userId) {
        return itemService.getItems(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto add(@RequestHeader("X-Sharer-User-Id")
                       @Positive(message = "id должен быть положительным") long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable @Positive(message = "id должен быть положительным") long itemId) {
        return itemService.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable
                              @Positive(message = "id должен быть положительным") long itemId,
                              @RequestBody ItemDtoUpdate itemUpdate) {
        return itemService.updateItem(userId, itemId, itemUpdate);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id")
                           @Positive(message = "id должен быть положительным") long userId,
                           @PathVariable(name = "itemId")
                           @Positive(message = "id должен быть положительным") long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam(defaultValue = "") String text) {
        return itemService.search(text);
    }
}
