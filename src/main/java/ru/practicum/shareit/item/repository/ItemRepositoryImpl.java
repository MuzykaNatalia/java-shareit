package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();
    private Long id = 1L;

    @Override
    public Item getItemById(Long itemId, Long userId) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Item> getAllItemUser(Long userId) {
        if (items.containsKey(userId)) {
            return items.get(userId);
        }
        return null;
    }

    @Override
    public Item createItem(Item item) {
        item.setOwner(item.getOwner());
        item.setId(id++);
        List<Item> itemUser = new ArrayList<>();
        if (items.containsKey(item.getOwner().getId())) {
            itemUser = items.get(item.getOwner().getId());
        }
        itemUser.add(item);
        items.put(item.getOwner().getId(), itemUser);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        List<Item> itemsUser = items.get(item.getOwner().getId()).stream()
                .filter(o -> !o.getId().equals(item.getId()))
                .collect(Collectors.toList());
        itemsUser.add(item);
        items.put(item.getOwner().getId(), itemsUser);
        return item;
    }

    @Override
    public Collection<Item> searchItems(String text) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }
}