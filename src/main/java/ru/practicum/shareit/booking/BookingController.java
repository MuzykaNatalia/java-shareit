package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.dto.validator.state.ValidState;
import ru.practicum.shareit.booking.service.BookingService;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@Valid @RequestBody BookingDtoCreate bookingDtoCreate,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.createBooking(bookingDtoCreate, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable @Positive @NotNull Long bookingId,
                                    @RequestParam @NotNull Boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getOneBookingUser(@PathVariable @Positive @NotNull Long bookingId,
                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getOneBookingUser(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> getAllBookingsBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(defaultValue = "ALL")
                                                       @ValidState String state,
                                                       @RequestParam(defaultValue = "0", required = false)
                                                       @Min(0) Integer from,
                                                       @RequestParam(defaultValue = "10", required = false)
                                                       @Min(1) Integer size) {
        return bookingService.getAllBookingsBooker(userId, BookingState.valueOf(state), from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllBookingsOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(defaultValue = "ALL")
                                                      @ValidState String state,
                                                      @RequestParam(defaultValue = "0", required = false)
                                                      @Min(0) Integer from,
                                                      @RequestParam(defaultValue = "10", required = false)
                                                      @Min(1) Integer size) {
        return bookingService.getAllBookingsOwner(userId, BookingState.valueOf(state), from, size);
    }
}
