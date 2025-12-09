package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserMapper userMapper;
    private final CommentRepository commentRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.mapToUserDtoList(repository.findAll());
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UserDtoUpdate userUpdate) {
        User exUser = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        if (userUpdate.getEmail() != null && !userUpdate.getEmail().equals(exUser.getEmail())) {
            if (repository.existsByEmailAndIdNot(userUpdate.getEmail(), userId)) {
                throw new DuplicatedDataException("Этот email уже используется другим пользователем");
            }
            exUser.setEmail(userUpdate.getEmail());
        }

        if (userUpdate.getName() != null) {
            exUser.setName(userUpdate.getName());
        }

        repository.save(exUser);
        return userMapper.mapToUserDto(exUser);
    }

    @Override
    public Optional<UserDto> getUserById(long id) {
        return repository.findById(id).map(userMapper::mapToUserDto);
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        if (repository.existsByEmail(userDto.getEmail())) {
            throw new DuplicatedDataException("Этот email уже используется");
        }

        User user = userMapper.mapToUser(userDto);
        return userMapper.mapToUserDto(repository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        List<Item> userItems = itemRepository.findByUserId(userId);
        List<Long> userItemIds = userItems.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        if (!userItemIds.isEmpty()) {
            commentRepository.deleteAllByItemId(userItemIds);

            bookingRepository.deleteAllByItemIdIn(userItemIds);

            itemRepository.deleteAllByOwnerId(userId);
        }

        bookingRepository.deleteAllByBookerId(userId);

        commentRepository.deleteAllByAuthorId(userId);

        repository.delete(user);
    }
}