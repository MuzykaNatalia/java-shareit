package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import static ru.practicum.shareit.Constant.*;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                    @RequestHeader(HEADER_USER) @Positive Long userId) {
        return itemRequestClient.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getListOfRequestsForItemsUser(@RequestHeader(HEADER_USER) @Positive Long userId) {
        return itemRequestClient.getListOfRequestsForItemsUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsPageByPage(@RequestParam(defaultValue = PAGE_FROM_DEFAULT)
                                                              @Min(0) Integer from,
                                                              @RequestParam(defaultValue = PAGE_SIZE_DEFAULT)
                                                              @Min(1) Integer size,
                                                              @RequestHeader(HEADER_USER) Long userId) {
        return itemRequestClient.getItemRequestsPageByPage(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@PathVariable @Positive Long requestId,
                                                 @RequestHeader(HEADER_USER) @Positive Long userId) {
        return itemRequestClient.getItemRequestById(requestId, userId);
    }
}
