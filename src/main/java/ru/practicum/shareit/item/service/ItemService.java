package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.*;
import java.util.Collection;

public interface ItemService {
    ItemDtoInfo getItemDtoById(Long itemId, Long userId);

    Collection<ItemDtoInfo> getAllItemUser(Long userId, Integer from, Integer size);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    Collection<ItemDto> searchItems(String text, Integer from, Integer size);

    CommentDto createComment(CommentDto commentDto, Long userId, Long itemId);
}
