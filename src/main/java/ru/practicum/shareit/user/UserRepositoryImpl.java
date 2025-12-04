package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users;

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return users.values().stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(getIdForUser());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    private long getIdForUser() {
        long id = users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return ++id;
    }
}