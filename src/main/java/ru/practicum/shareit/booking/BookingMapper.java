package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "WAITING")
    Booking mapToBooking(CreateBookingDto dto, Item item, User booker);

    @Mapping(target = "item", source = "item")
    BookingDto mapToBookingDto(Booking booking);

    List<BookingDto> mapToBookingDtoList(List<Booking> bookings);
}