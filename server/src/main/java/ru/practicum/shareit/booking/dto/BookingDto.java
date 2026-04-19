package ru.practicum.shareit.booking.dto;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemResponseDto item;
    private UserDto booker;
    private Status status;
}
