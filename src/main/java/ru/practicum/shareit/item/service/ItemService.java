package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import java.util.Collection;

public interface ItemService {
    ItemDtoInfo getItemById(Long itemId, Long userId);

    Item getItemByIdAvailable(Long itemId, Long userId);

    Collection<ItemDtoInfo> getAllItemUser(Long userId);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    Collection<ItemDto> searchItems(String text);

    CommentDto createComment(CommentDto commentDto, Long userId, Long itemId);
}
