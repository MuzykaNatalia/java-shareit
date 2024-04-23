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

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.getItemById(itemId, userId);
        if (item == null) {
            log.warn("The item with this id={} not found", itemId);
            throw new NotFoundException("The item with this id=" + itemId + " not found");
        }
        log.info("The item with id={} was received user with id={}", itemId, userId);
        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getAllItemUser(Long userId) {
        Collection<Item> items = itemRepository.getAllItemUser(userId);
        log.info("All items have been received");
        return itemMapper.listToItemDto(items);
    }

    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = isExistUser(userId);
        Item item = itemRepository.createItem(itemMapper.toItem(itemDto, user));
        log.info("Item has been created={}", item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDtoNew) {
        User user = isExistUser(userId);
        ItemDto itemDtoOld = getItemById(itemId, userId);
        if (!itemDtoOld.getOwnerId().equals(userId)) {
            log.warn("The item with this id={} not found", itemId);
            throw new NotFoundException("The item with this id=" + itemId + " not found");
        }
        setItemDto(itemDtoOld, itemDtoNew);
        Item item = itemRepository.updateItem(itemMapper.toItem(itemDtoOld, user));
        log.info("Item has been updated={}", item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        Collection<Item> items = itemRepository.searchItems(text);
        log.info("Items={} by text={} received", items, text);
        return itemMapper.listToItemDto(items);
    }

    private User isExistUser(Long userId) {
        User user = userMapper.toUser(userService.getUserById(userId));
        if (user == null) {
            log.warn("The user with this id={} not found", userId);
            throw new NotFoundException("The user with this id=" + userId + " not found");
        }
        return user;
    }

    private void setItemDto(ItemDto itemDtoOld, ItemDto itemDtoNew) {
        if (itemDtoNew.getName() != null && !itemDtoNew.getName().isEmpty()) {
            itemDtoOld.setName(itemDtoNew.getName());
        }
        if (itemDtoNew.getDescription() != null && !itemDtoNew.getDescription().isEmpty()) {
            itemDtoOld.setDescription(itemDtoNew.getDescription());
        }
        if (itemDtoNew.getAvailable() != null) {
            itemDtoOld.setAvailable(itemDtoNew.getAvailable());
        }
    }
}