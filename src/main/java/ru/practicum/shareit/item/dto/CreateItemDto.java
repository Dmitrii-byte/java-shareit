package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateItemDto {
    private Long id;
    @NotBlank(message = "не может быть пустым")
    private String name;
    @NotBlank(message = "не может быть пустым")
    private String description;
    @NotNull(message = "должна быть указана")
    private Boolean available;
}