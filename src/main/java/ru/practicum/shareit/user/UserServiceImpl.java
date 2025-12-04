package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.mapToItemDto(repository.findAll());
    }

    @Override
    public UserDto updateUser(long userId, UserDtoUpdate userUpdate) {
        User exUser = repository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        if (emailExist(userUpdate.getEmail())) {
            throw new DuplicatedDataException("Этот email уже используется");
        }
        if (userUpdate.getEmail() != null) {
            exUser.setEmail(userUpdate.getEmail());
        }
        if (userUpdate.getName() != null) {
            exUser.setName(userUpdate.getName());
        }
        repository.save(exUser);
        return UserMapper.mapToUserDto(exUser);
    }

    @Override
    public Optional<UserDto> getUserById(long id) {
        return Optional.of(UserMapper.mapToUserDto(repository.getUserById(id).get()));
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        if (emailExist(userDto.getEmail())) {
            throw new DuplicatedDataException("Этот email уже используется");
        }
        return UserMapper.mapToUserDto(repository.save(UserMapper.mapToUser(userDto)));
    }

    @Override
    public void deleteUser(long userId) {
        if (userNotExist(userId)) {
            throw new NotFoundException("Пользователь с " + userId + " не найден");
        }
        repository.deleteUser(userId);
    }

    private boolean userNotExist(long userId) {
        return repository.findAll().stream()
                .noneMatch(u -> u.getId() == userId);
    }

    private boolean emailExist(String email) {
        return repository.findAll().stream()
                .anyMatch(users -> users.getEmail() != null &&
                        users.getEmail().equals(email));
    }
}