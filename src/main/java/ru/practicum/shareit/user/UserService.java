package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;
import java.util.Optional;

interface UserService {
    List<UserDto> getAllUsers();

    Optional<UserDto> getUserById(long id);

    UserDto updateUser(long userId, UserDtoUpdate userUpdate);

    UserDto saveUser(UserDto userDto);

    void deleteUser(long userId);
}