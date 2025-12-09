package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getALlUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public Optional<UserDto> getUserById(@PathVariable
                                         @Positive(message = "id должен быть положительным") long userId) {
        return userService.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable
                              @Positive(message = "id должен быть положительным") long userId,
                              @RequestBody UserDtoUpdate userUpdate) {
        return userService.updateUser(userId, userUpdate);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@RequestBody @Valid UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable
                           @Positive(message = "id должен быть положительным") long userId) {
        userService.deleteUser(userId);
    }
}