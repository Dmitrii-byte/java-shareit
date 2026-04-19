package ru.practicum.shareit.request.dto;

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
