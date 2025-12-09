package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.mapToUserDtoList(repository.findAll());
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
        return UserMapper.mapToUserDto(exUser);
    }

    @Override
    public Optional<UserDto> getUserById(long id) {
        return repository.findById(id).map(UserMapper::mapToUserDto);
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto userDto) {
        if (repository.existsByEmail(userDto.getEmail())) {
            throw new DuplicatedDataException("Этот email уже используется");
        }

        User user = UserMapper.mapToUser(userDto);
        return UserMapper.mapToUserDto(repository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        List<Long> userItemIds = itemRepository.findItemIdsByUserId(userId);

        if (!userItemIds.isEmpty()) {
            commentRepository.deleteByItemIdIn(userItemIds);
            bookingRepository.deleteByItemIdIn(userItemIds);
            itemRepository.deleteByOwnerId(userId);
        }
        bookingRepository.deleteByBookerId(userId);
        repository.deleteById(userId);
    }
}