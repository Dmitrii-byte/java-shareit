package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemResponseDto> get(@RequestHeader("X-Sharer-User-Id")
                                     @Positive(message = "id должен быть положительным") long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto add(@RequestHeader("X-Sharer-User-Id")
                               @Positive(message = "id должен быть положительным") long userId,
                               @Valid @RequestBody CreateItemDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@RequestHeader("X-Sharer-User-Id")
                                       @Positive(message = "id должен быть положительным") long userId,
                                       @PathVariable
                                       @Positive(message = "id должен быть положительным") long itemId) {
        return itemService.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader("X-Sharer-User-Id")
                                      @Positive(message = "id должен быть положительным") long userId,
                                      @PathVariable
                                      @Positive(message = "id должен быть положительным") long itemId,
                                      @RequestBody ItemUpdateDto itemUpdate) {
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
    public List<ItemResponseDto> searchItem(@RequestParam(defaultValue = "") String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @Valid @RequestBody CommentCreateDto commentCreateDto) {
        return itemService.addComment(userId, itemId, commentCreateDto);
    }
}
