package ru.practicum.shareit.request;   // или твой пакет

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addNewRequest(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Add request for userId={}", userId);
        return itemRequestClient.addNewRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Get requests for userId={}", userId);
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") @Positive long userId) {
        log.info("Get all requests for userId={}", userId);
        return itemRequestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId) {
        log.info("Get request by id={}", requestId);
        return itemRequestClient.getRequestById(requestId);
    }
}
