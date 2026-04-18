package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@UtilityClass
public class BookingMapper {

    public static Booking mapToBooking(CreateBookingDto dto, Item item, User booker) {
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);
        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.mapToItemResponseDto(booking.getItem(), List.of()), // Комментарии пока не нужны
                UserMapper.mapToUserDto(booking.getBooker()),
                booking.getStatus()
        );
    }

    public static List<BookingDto> mapToBookingDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }
}