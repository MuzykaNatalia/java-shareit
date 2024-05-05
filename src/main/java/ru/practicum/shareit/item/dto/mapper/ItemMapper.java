package ru.practicum.shareit.item.dto.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoInfo;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoInfo;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.user.model.User;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public Item toItem(ItemDto itemDto, User owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .build();
    }

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public ItemDtoInfo toItemDtoInfo(Item item, Map<Long, List<CommentDto>> comments) {
        return ItemDtoInfo.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments.isEmpty() ? new ArrayList<>() : comments.get(item.getId()))
                .build();
    }

    public ItemDtoInfo toItemDtoInfo(Item item, BookingDtoInfo next, BookingDtoInfo last, List<CommentDto> comments) {
        return ItemDtoInfo.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .nextBooking(next)
                .lastBooking(last)
                .comments(comments != null ? comments : new ArrayList<>())
                .build();
    }

    public Collection<ItemDto> toItemDto(Collection<Item> items) {
        return items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }
}
