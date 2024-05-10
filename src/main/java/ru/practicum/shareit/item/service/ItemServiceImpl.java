package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoInfo;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comment.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import javax.validation.ValidationException;
import static ru.practicum.shareit.booking.BookingStatus.APPROVED;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private static final String NEXT = "next";
    private static final String LAST = "last";

    @Transactional(readOnly = true)
    @Override
    public ItemDtoInfo getItemDtoById(Long itemId, Long userId) {
        Item item = getItemById(itemId, userId);
        List<Comment> comments = commentRepository.findByItem_Id(itemId).orElse(new ArrayList<>());
        Map<Long, List<CommentDto>> commentsItem = getCommentDtoSortByIdItem(comments);
        boolean isOwner = itemRepository.existsByIdAndOwner_Id(itemId, userId);
        if (!isOwner) {
            List<CommentDto> commentDto = commentsItem.isEmpty() ? new ArrayList<>() : commentsItem.get(item.getId());
            return itemMapper.toOneItemDtoInfoForAllUsers(item, commentDto);
        }
        log.info("Information about the item id={} was obtained by the user id={}", itemId, userId);
        return setBookingsForOwner(List.of(item), List.of(itemId), commentsItem).stream().findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public Item getItemByIdAvailable(Long itemId, Long userId) {
        Item item = getItemById(itemId, userId);
        if (item.getAvailable().equals(false)) {
            log.warn("The item with id={} not found or not available", itemId);
            throw new ValidationException("The item with this id=" + itemId + " not found or not available");
        }
        log.info("Information about the item id={} was obtained by the user id={}", itemId, userId);
        return item;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<ItemDtoInfo> getAllItemUser(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        List<Long> itemsId = items.stream().map(Item::getId).collect(Collectors.toList());
        List<Comment> comments = commentRepository.findAllByItem_IdIn(itemsId).orElse(new ArrayList<>());
        Map<Long, List<CommentDto>> commentsItems = getCommentDtoSortByIdItem(comments);
        log.info("All items have been received");
        return setBookingsForOwner(items, itemsId, commentsItems);
    }

    @Transactional
    @Override
    public ItemDto createItem(Long userId, ItemDto itemDto) {
        User user = userMapper.toUser(userService.getUserDtoById(userId));
        Item item = itemRepository.save(itemMapper.toItem(itemDto, user));
        log.info("Item has been created={}", item);
        return itemMapper.toItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDtoNew) {
        User user = userMapper.toUser(userService.getUserDtoById(userId));
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
        return itemMapper.toItemDtoCollection(items);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long userId, Long itemId) {
        User user = userService.getUserById(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            log.warn("A user={} wants to leave a review for an item id={} that doesn't exist", userId, itemId);
            throw new ValidationException("The item doesn't exist yet");
        });
        isBookerOfThisItem(userId, itemId);
        commentDto.setCreated(LocalDateTime.now());

        Comment comment = commentMapper.toComment(commentDto, user, item);
        comment = commentRepository.save(comment);
        log.info("Created comment id={} about item={} by user id={}", comment.getId(), itemId, userId);
        return commentMapper.toCommentDto(comment);
    }

    private Item getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            log.warn("The item with this id={} not found for user id={}", itemId, userId);
            throw new NotFoundException("The item with this id=" + itemId + " not found");
        });

        log.info("The item with id={} was received user with id={}", itemId, userId);
        return item;
    }

    private void setItemDto(Item itemOld, ItemDto itemDtoNew, User owner) {
        if (itemDtoNew.getName() != null && !itemDtoNew.getName().isEmpty()) {
            itemOld.setName(itemDtoNew.getName());
        }
        if (itemDtoNew.getDescription() != null && !itemDtoNew.getDescription().isEmpty()) {
            itemOld.setDescription(itemDtoNew.getDescription());
        }
        if (itemDtoNew.getAvailable() != null) {
            itemOld.setAvailable(itemDtoNew.getAvailable());
        }
        itemOld.setOwner(owner);
    }

    private Collection<ItemDtoInfo> setBookingsForOwner(List<Item> items, List<Long> itemsId,
                                                        Map<Long, List<CommentDto>> commentsItem) {
        LocalDateTime current = LocalDateTime.now();
        List<Booking> nextBookings = bookingRepository.findNextBookingsForOwner(current, itemsId, APPROVED);
        List<Booking> lastBookings = bookingRepository.findLastBookingsForOwner(current, itemsId, APPROVED);
        return getItemDtoInfoForOwner(items, nextBookings, lastBookings, commentsItem);
    }

    private Collection<ItemDtoInfo> getItemDtoInfoForOwner(List<Item> items, List<Booking> next, List<Booking> last,
                                                     Map<Long, List<CommentDto>> commentsItem) {
        Map<String, Map<Long, BookingDtoInfo>> booking = getBookingDtoInfoMapByNextAndLast(next, last);
        Map<Long, BookingDtoInfo> nextBooking = booking.get(NEXT);
        Map<Long, BookingDtoInfo> lastBooking = booking.get(LAST);
        return items.stream()
                .map(item -> {
                    BookingDtoInfo nextDto = null;
                    BookingDtoInfo lastDto = null;
                    List<CommentDto> commentDto = new ArrayList<>();
                    if (nextBooking != null && nextBooking.containsKey(item.getId())) {
                        nextDto = nextBooking.get(item.getId());
                    }
                    if (lastBooking != null && lastBooking.containsKey(item.getId())) {
                        lastDto = lastBooking.get(item.getId());
                    }
                    if (commentsItem.containsKey(item.getId())) {
                        commentDto = commentsItem.get(item.getId());
                    }
                    return itemMapper.toOneItemDtoInfoForOwner(item, nextDto, lastDto, commentDto);
                }).sorted(Comparator.comparing(ItemDtoInfo::getId))
                .collect(Collectors.toList());
    }

    private Map<String, Map<Long, BookingDtoInfo>> getBookingDtoInfoMapByNextAndLast(List<Booking> nextBookings,
                                                                                     List<Booking> lastBookings) {
        List<BookingDtoInfo> nextBookingDtoInfo = bookingMapper.toBookingDtoInfoList(nextBookings);
        List<BookingDtoInfo> lastBookingDtoInfo = bookingMapper.toBookingDtoInfoList(lastBookings);

        Map<Long, BookingDtoInfo> next = bookingMapper.toBookingDtoInfoMapByIdItem(nextBookingDtoInfo);
        Map<Long, BookingDtoInfo> last = bookingMapper.toBookingDtoInfoMapByIdItem(lastBookingDtoInfo);
        Map<String, Map<Long, BookingDtoInfo>> result = new HashMap<>();

        if (!next.isEmpty()) {
            result.put(NEXT, next);
        }
        if (!last.isEmpty()) {
            result.put(LAST, last);
        }
        return result;
    }

    private Map<Long, List<CommentDto>> getCommentDtoSortByIdItem(List<Comment> comments) {
        List<CommentDto> allCommentDtoItems = commentMapper.toCommentDtoList(comments);
        Map<Long, List<CommentDto>> result = new HashMap<>();
        allCommentDtoItems.forEach(commentDto -> {
            List<CommentDto> comment = new ArrayList<>();
            if (result.containsKey(commentDto.getItemId())) {
                comment = result.get(commentDto.getItemId());
                comment.add(commentDto);
            } else {
                comment.add(commentDto);
            }
            result.put(commentDto.getItemId(), comment);
        });
        return result;
    }

    private void isBookerOfThisItem(Long userId, Long itemId) {
        boolean isValid = bookingRepository.existsByItemIdAndBookerIdAndStatusAndEndBefore(
                itemId, userId, APPROVED, LocalDateTime.now());
        if (!isValid) {
            throw new ValidationException("Only users whose booking has expired can leave comments");
        }
    }
}
