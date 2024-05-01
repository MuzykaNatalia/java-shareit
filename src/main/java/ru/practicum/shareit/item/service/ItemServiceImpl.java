package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final UserService userService;
    @Autowired
    private final ItemMapper itemMapper;
    @Autowired
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).stream().findFirst().orElse(null);
        if (item == null) {
            log.warn("The item with this id={} not found", itemId);
            throw new NotFoundException("The item with this id=" + itemId + " not found");
        }
        log.info("The item with id={} was received user with id={}", itemId, userId);
        return itemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemDto> getAllItemUser(Long userId) {
        Collection<Item> items = itemRepository.findAllByOwnerId(userId);
        log.info("All items have been received");
        return itemMapper.listToItemDto(items);
    }

    @Transactional
    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = userMapper.toUser(userService.getUserById(userId));
        Item item = itemRepository.save(itemMapper.toItem(itemDto, user));
        log.info("Item has been created={}", item);
        return itemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDtoNew) {
        User user = userMapper.toUser(userService.getUserById(userId));
        Item itemOld = itemRepository.findById(itemId).stream().findFirst().orElse(null);
        if (itemOld == null || !itemOld.getOwner().getId().equals(userId)) {
            log.warn("The item with this id={} not found", itemId);
            throw new NotFoundException("The item with this id=" + itemId + " not found");
        }
        setItemDto(itemOld, itemDtoNew, user);
        Item item = itemRepository.save(itemOld);
        log.info("Item has been updated={}", item);
        return itemMapper.toItemDto(item);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<Item> items = itemRepository.searchItems(text);
        log.info("Items={} by text={} received", items, text);
        return itemMapper.listToItemDto(items);
    }

    private void setItemDto(Item itemOld, ItemDto itemDtoNew, User user) {
        if (itemDtoNew.getName() != null && !itemDtoNew.getName().isEmpty()) {
            itemOld.setName(itemDtoNew.getName());
        }
        if (itemDtoNew.getDescription() != null && !itemDtoNew.getDescription().isEmpty()) {
            itemOld.setDescription(itemDtoNew.getDescription());
        }
        if (itemDtoNew.getAvailable() != null) {
            itemOld.setAvailable(itemDtoNew.getAvailable());
        }
        itemOld.setOwner(user);
    }
}
