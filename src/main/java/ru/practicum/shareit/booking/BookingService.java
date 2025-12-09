package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(long userId, CreateBookingDto dto);

    BookingDto getBookingById(Long userId, long bookingId);

    BookingDto approveOrRejectBooking(long userId, long bookingId, boolean approved);

    List<BookingDto> getAllByBooker(long bookerId, String state);

    List<BookingDto> getAllByOwner(long ownerId, String state);
}