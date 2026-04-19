package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public List<ItemResponseDto> getItemsByUserId(long userId) {
        List<Item> items = repository.findByUserId(userId);
        LocalDateTime now = LocalDateTime.now();

        List<Long> itemIds = items.stream().map(Item::getId).toList();

        Map<Long, List<CommentDto>> commentsMap = commentRepository.findByItemIdIn(itemIds).stream().collect(Collectors.groupingBy(comment -> comment.getItem().getId(), Collectors.mapping(CommentMapper::mapToCommentDto, Collectors.toList())));

        Map<Long, List<Booking>> lastBookingsMap = bookingRepository.findLastBookingsForItems(itemIds, Status.APPROVED, now).stream().collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        Map<Long, List<Booking>> nextBookingsMap = bookingRepository.findNextBookingsForItems(itemIds, Status.APPROVED, now).stream().collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        List<ItemResponseDto> dtos = new ArrayList<>();
        for (Item item : items) {
            LocalDateTime last = lastBookingsMap.getOrDefault(item.getId(), List.of()).stream().findFirst().map(Booking::getEnd).orElse(null);

            LocalDateTime next = nextBookingsMap.getOrDefault(item.getId(), List.of()).stream().findFirst().map(Booking::getStart).orElse(null);

            List<CommentDto> comments = commentsMap.getOrDefault(item.getId(), List.of());
            dtos.add(ItemMapper.mapToItemResponseDto(item, last, next, comments));
        }
        return dtos;
    }

    @Override
    @Transactional
    public ItemResponseDto addNewItem(long userId, CreateItemDto itemDto) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(owner);

        // ← НОВЫЙ БЛОК: связываем с запросом, если requestId передан
        if (itemDto.getRequestId() != null) {
            ItemRequest request = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с id " + itemDto.getRequestId() + " не найден"));

            item.setRequest(request);
        }

        Item savedItem = repository.save(item);
        return ItemMapper.mapToItemResponseDto(savedItem, List.of());
    }

    @Override
    public ItemResponseDto getItemById(long itemId, long userId) {
        Item item = repository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));

        List<CommentDto> comments = commentRepository.findByItemId(itemId).stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();

        LocalDateTime last = null;
        LocalDateTime next = null;

        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();

            Optional<Booking> lastBooking = bookingRepository.findByItemIdAndStatusAndEndBeforeOrderByEndDesc(item.getId(), Status.APPROVED, now).stream().findFirst();

            Optional<Booking> nextBooking = bookingRepository.findByItemIdAndStatusAndStartAfterOrderByStartAsc(item.getId(), Status.APPROVED, now).stream().findFirst();

            last = lastBooking.map(Booking::getEnd).orElse(null);
            next = nextBooking.map(Booking::getStart).orElse(null);
        }
        return ItemMapper.mapToItemResponseDto(item, last, next, comments);
    }

    @Override
    @Transactional
    public ItemResponseDto updateItem(long userId, long itemId, ItemUpdateDto itemUpdate) {
        Item item = repository.findById(itemId).orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вы не владелец предмета с id " + itemId);
        }

        if (itemUpdate.getName() != null) {
            item.setName(itemUpdate.getName());
        }
        if (itemUpdate.getDescription() != null) {
            item.setDescription(itemUpdate.getDescription());
        }
        if (itemUpdate.getAvailable() != null) {
            item.setAvailable(itemUpdate.getAvailable());
        }

        Item updatedItem = repository.save(item);
        List<CommentDto> comments = commentRepository.findByItemId(itemId).stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();

        return ItemMapper.mapToItemResponseDto(updatedItem, comments);
    }

    @Override
    public List<ItemResponseDto> search(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        String lowerText = text.toLowerCase();
        List<Item> items = repository.findItemByParametrs(lowerText);

        List<Long> itemIds = items.stream().map(Item::getId).toList();

        Map<Long, List<CommentDto>> commentsMap = commentRepository.findByItemIdIn(itemIds)
                .stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId(), Collectors.mapping(CommentMapper::mapToCommentDto, Collectors.toList())));

        return ItemMapper.mapToItemResponseDto(items, commentsMap);
    }

    @Override
    @Transactional
    public CommentDto addComment(long userId, long itemId, CommentCreateDto commentCreateDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));

        List<Booking> userBookings = bookingRepository
                .findByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId, Status.APPROVED, LocalDateTime.now());

        if (userBookings.isEmpty()) {
            throw new ConditionsNotMetException("Пользователь не брал данный предмет в аренду или аренда еще не завершена");
        }

        Comment comment = CommentMapper.mapToComment(commentCreateDto, item, author);
        Comment savedComment = commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteItem(long userId, long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new ConditionsNotMetException("Вы не владелец предмета с id " + itemId);
        }

        List<Booking> bookings = bookingRepository.findByItemId(item.getId());
        if (!bookings.isEmpty()) {
            bookingRepository.deleteAll(bookings);
        }

        List<Comment> comments = commentRepository.findByItemId(item.getId());
        if (!comments.isEmpty()) {
            commentRepository.deleteAll(comments);
        }
        repository.deleteById(itemId);
    }
}