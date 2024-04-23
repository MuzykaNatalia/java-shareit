package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;
import java.util.Collection;

public interface ItemRepository {
    Item getItemById(Long itemId, Long userId);

    Collection<Item> getAllItemUser(Long userId);

    Item createItem(Item item);

    Item updateItem(Item item);

    Collection<Item> searchItems(String text);
}