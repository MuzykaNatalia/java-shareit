package ru.practicum.shareit.item.dto.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.util.*;

public interface ItemMapper {
    Item toItem(ItemDto itemDto, User owner);

    ItemDto toItemDto(Item item);

    List<ItemDto> listToItemDto(Collection<Item> items);
}