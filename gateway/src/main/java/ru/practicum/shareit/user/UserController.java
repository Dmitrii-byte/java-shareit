package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getALlUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable @Positive long userId) {
        log.info("Get user by id={}", userId);
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @Positive long userId,
                                             @RequestBody UserDtoUpdate clientUpdate) {
        log.info("Update user id={}", userId);
        return userClient.updateUser(userId, clientUpdate);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto clientDto) {
        log.info("Create user {}", clientDto);
        return userClient.saveUser(clientDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Positive long userId) {
        log.info("Delete user id={}", userId);
        return userClient.deleteUser(userId);
    }
}
