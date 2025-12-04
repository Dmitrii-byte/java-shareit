package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@UtilityClass
public class UserMapper {
    public static User mapToUser(UserDto userDto) {
        User user = new User();
        user.setId(user.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static List<UserDto> mapToItemDto(List<User> users) {
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }
}