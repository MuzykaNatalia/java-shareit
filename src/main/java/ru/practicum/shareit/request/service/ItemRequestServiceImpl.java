package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.*;
import ru.practicum.shareit.request.dto.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDtoInfo createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User requester = getUserIfTheExists(userId);
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto, requester, LocalDateTime.now());
        ItemRequest createdItemRequest = itemRequestRepository.save(itemRequest);

        log.info("Request id={} created by user id={}", createdItemRequest.getId(), userId);
        return itemRequestMapper.toItemRequestDtoInfo(createdItemRequest);
    }

    @Override
    public List<ItemRequestDtoInfo> getListOfRequestsForItemsUser(Long userId) {
        getUserIfTheExists(userId);
        List<ItemRequest> allRequestsUser = itemRequestRepository.findAllByRequester_IdOrderByCreatedDesc(userId);

        log.info("The list of requests for items was received by a user with id={}", userId);
        return itemRequestMapper.toItemRequestDtoInfoList(allRequestsUser);
    }

    @Override
    public List<ItemRequestDtoInfo> getItemRequestsPageByPage(Integer from, Integer size, Long userId) {
        getUserIfTheExists(userId);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Order.desc("created")));
        List<ItemRequest> requests = itemRequestRepository.findAllByRequester_IdNot(userId, pageable);

        log.info("The list of requests for items was received by a user with id={}", userId);
        return itemRequestMapper.toItemRequestDtoInfoList(requests);
    }

    @Override
    public ItemRequestDtoInfo getItemRequestById(Long requestId, Long userId) {
        getUserIfTheExists(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(() -> {
            log.warn("Request id={} for user id={} not found", requestId, userId);
            throw new NotFoundException("Request id=" + requestId + " not found");
        });

        log.info("Request for items was received by a user with id={}", userId);
        return itemRequestMapper.toItemRequestDtoInfo(itemRequest);
    }

    private User getUserIfTheExists(Long userId) {
        return userRepository.findById(userId).stream().findFirst().orElseThrow(() -> {
            log.warn("User with id={} not found", userId);
            throw new NotFoundException("User with id=" + userId + " not found");
        });
    }
}
