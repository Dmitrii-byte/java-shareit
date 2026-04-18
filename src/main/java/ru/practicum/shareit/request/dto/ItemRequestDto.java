package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@ToString
public class ItemRequestDto {

    private Long id;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    private Instant created;

    private List<Item> items;

    @Getter
    @Setter
    @ToString
    public static class Item {
        private Long id;
        private String name;
        private Long ownerId;
    }
}
