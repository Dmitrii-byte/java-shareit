package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto createBooking(long bookerId, CreateBookingDto dto) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (bookerId == item.getOwner().getId()) {
            throw new ValidationException("Нельзя бронировать свою вещь");
        }
        if (Boolean.FALSE.equals(item.getAvailable())) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }
        if (!dto.getEnd().isAfter(dto.getStart())) {
            throw new ValidationException("Дата окончания должна быть позже даты начала");
        }

        Booking booking = BookingMapper.mapToBooking(dto, item, booker);
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto approveOrRejectBooking(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ValidationException("Только владелец может подтверждать/отклонять бронирование");
        }
        if (booking.getStatus() != Status.WAITING) {
            throw new ValidationException("Статус уже изменён");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        Long ownerId = booking.getItem().getOwner().getId();
        Long bookerId = booking.getBooker().getId();

        if (!userId.equals(ownerId) && !userId.equals(bookerId)) {
            throw new ValidationException("Доступ запрещён");
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBooker(long bookerId, String state) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(bookerId);
        return filterBookings(bookings, state);
    }

    @Override
    public List<BookingDto> getAllByOwner(long ownerId, String state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        List<Booking> bookings = bookingRepository.findByOwnerId(ownerId);
        return filterBookings(bookings, state);
    }

    private List<BookingDto> filterBookings(List<Booking> bookings, String stateParam) {
        String state = (stateParam == null || stateParam.isBlank()) ? "ALL" : stateParam.toUpperCase();
        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case "ALL" -> BookingMapper.mapToBookingDtoList(bookings);
            case "CURRENT" -> bookings.stream()
                    .filter(b -> !b.getStart().isAfter(now) && !b.getEnd().isBefore(now))
                    .map(BookingMapper::mapToBookingDto)
                    .toList();
            case "PAST" -> bookings.stream()
                    .filter(b -> b.getEnd().isBefore(now))
                    .map(BookingMapper::mapToBookingDto)
                    .toList();
            case "FUTURE" -> bookings.stream()
                    .filter(b -> b.getStart().isAfter(now))
                    .map(BookingMapper::mapToBookingDto)
                    .toList();
            case "WAITING" -> bookings.stream()
                    .filter(b -> b.getStatus() == Status.WAITING)
                    .map(BookingMapper::mapToBookingDto)
                    .toList();
            case "REJECTED" -> bookings.stream()
                    .filter(b -> b.getStatus() == Status.REJECTED || b.getStatus() == Status.CANCELED)
                    .map(BookingMapper::mapToBookingDto)
                    .toList();
            default -> throw new ValidationException("Unknown state: " + stateParam);
        };
    }
}