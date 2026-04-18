package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                         @Valid @RequestBody CreateBookingDto dto) {
        log.info("Creating booking for userId={}, dto={}", userId, dto);
        return bookingClient.create(userId, dto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam boolean approved) {
        log.info("Approving bookingId={} by userId={}, approved={}", bookingId, userId, approved);
        return bookingClient.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                          @PathVariable long bookingId) {
        log.info("Get bookingId={} by userId={}", bookingId, userId);
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookerBookings(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                    @RequestParam(defaultValue = "ALL") String state) {
        log.info("Get booker bookings for userId={}, state={}", userId, state);
        return bookingClient.getBookerBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") @Positive long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        log.info("Get owner bookings for userId={}, state={}", userId, state);
        return bookingClient.getOwnerBookings(userId, state);
    }
}