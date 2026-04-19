package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateBookingDto {
    @NotNull(message = "itemId не может быть пустым")
    private Long itemId;

    @NotNull(message = "Дата начала обязательна")
    @FutureOrPresent(message = "Дата начала не может быть в прошлом")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания обязательна")
    @Future(message = "Дата окончания должна быть в будущем")
    private LocalDateTime end;

    @AssertTrue(message = "Дата окончания должна быть позже даты начала")
    public boolean isEndAfterStart() {
        return start != null && end != null && end.isAfter(start);
    }
}