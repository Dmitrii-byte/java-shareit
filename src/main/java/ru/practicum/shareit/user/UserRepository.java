package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();

    Optional<User> getUserById(long id);

    User save(User user);

    void deleteUser(long userId);
}