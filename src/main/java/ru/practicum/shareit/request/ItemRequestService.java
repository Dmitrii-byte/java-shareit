package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addNewRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getRequests(long userId);

    List<ItemRequestDto> getAllRequests(long userId);

    ItemRequestDto getRequestById(Long requestId);
}

