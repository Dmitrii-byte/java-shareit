package ru.practicum.shareit.request;

import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto addNewRequest(long userId, ItemRequestDto itemRequestDto) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto);
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(Instant.now());

        ItemRequest saved = repository.save(itemRequest);
        return ItemRequestMapper.mapToItemRequestDto(saved, List.of());
    }

    @Override
    public List<ItemRequestDto> getRequests(long userId) {
        List<ItemRequest> requests = repository.findByRequestorIdOrderByCreatedDesc(userId);
        return requests.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ItemRequestDto> getAllRequests(long userId) {
        List<ItemRequest> requests = repository.findAllByRequestorIdNotOrderByCreatedDesc(userId);
        return requests.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId) {
        ItemRequest request = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id " + requestId + " не найден"));
        return toDto(request);
    }

    private ItemRequestDto toDto(ItemRequest request) {
        List<Item> itemsList = itemRepository.findByRequestId(request.getId());
        List<ItemRequestDto.Item> items = itemsList.stream()
                .map(ItemRequestMapper::mapToItemForRequest)
                .toList();
        return ItemRequestMapper.mapToItemRequestDto(request, items);
    }
}
