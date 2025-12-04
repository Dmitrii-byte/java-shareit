package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @Email(message = "Некорректный формат email")
    @NotNull(message = "должен быть указан")
    private String email;
    @NotNull(message = "должно быть указано")
    @NotBlank(message = "не может быть пустым")
    private String name;
}