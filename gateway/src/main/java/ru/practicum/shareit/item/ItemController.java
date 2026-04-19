package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collections;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Get items for userId={}", userId);
        return itemClient.getItemsByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                      @Valid @RequestBody CreateItemDto itemDto) {
        log.info("Add item for userId={}, dto={}", userId, itemDto);
        return itemClient.addNewItem(userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                              @PathVariable @Positive long itemId) {
        log.info("Get itemId={} by userId={}", itemId, userId);
        return itemClient.getItemById(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                             @PathVariable @Positive long itemId,
                                             @RequestBody ItemUpdateDto itemUpdate) {
        log.info("Update itemId={} by userId={}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemUpdate);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                             @PathVariable @Positive long itemId) {
        log.info("Delete itemId={} by userId={}", itemId, userId);
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam(defaultValue = "") String text) {
        log.info("Search items with text={}", text);
        if (text == null || text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.info("Add comment to itemId={} by userId={}", itemId, userId);
        return itemClient.addComment(userId, itemId, commentCreateDto);
    }
}
