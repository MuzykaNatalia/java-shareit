package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.Create;
import ru.practicum.shareit.config.Update;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static ru.practicum.shareit.Constant.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable @Positive @NotNull Long itemId,
                                   @RequestHeader(HEADER_USER) Long userId) {
        log.info("GET: user request with id={} to view a item with id={}", userId, itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemUser(@RequestHeader(HEADER_USER) Long userId,
                                                  @RequestParam(defaultValue = PAGE_FROM_DEFAULT) @Min(0) Integer from,
                                                  @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) @Min(1) Integer size) {
        log.info("GET: user request with id={} to view a items. Page from={}, page size={}", userId, from, size);
        return itemClient.getAllItemUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@Validated(Create.class) @RequestBody ItemDto itemDto,
                                             @RequestHeader(HEADER_USER) Long userId) {
        log.info("POST: user request with id={} to create a item, request body={}", userId, itemDto);
        return itemClient.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Validated(Update.class) @RequestBody ItemDto itemDto,
                              @PathVariable @Positive @NotNull Long itemId,
                              @RequestHeader(HEADER_USER) Long userId) {
        log.info("PATCH: user request with id={} to update a item, request body={}", userId, itemDto);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @RequestHeader(HEADER_USER) Long userId,
                                              @RequestParam(defaultValue = PAGE_FROM_DEFAULT) @Min(0) Integer from,
                                              @RequestParam(defaultValue = PAGE_SIZE_DEFAULT) @Min(1) Integer size) {
        log.info("GET: user request with id={} to search a item with name or description={}", userId, text);
        return itemClient.searchItems(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                    @RequestHeader(HEADER_USER) Long userId,
                                    @PathVariable @Positive @NotNull Long itemId) {
        log.info("POST: user request with id={} to create a comment on an item with id={}, request body={}",
                userId, itemId, commentDto);
        return itemClient.createComment(commentDto, userId, itemId);
    }
}
