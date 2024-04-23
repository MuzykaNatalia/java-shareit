package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import java.util.Collection;

public interface ItemService {
    ItemDto getItemById(Long itemId, Long userId);

    Collection<ItemDto> getAllItemUser(Long userId);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    Collection<ItemDto> searchItems(String text);
}