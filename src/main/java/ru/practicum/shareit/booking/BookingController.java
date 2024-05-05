package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.dto.validator.state.ValidState;
import ru.practicum.shareit.booking.service.BookingService;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@Valid @RequestBody BookingDtoCreate bookingDtoCreate,
                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.createBooking(userId, bookingDtoCreate);
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
                                                       @ValidState String state) {
        return bookingService.getAllBookingsBooker(userId, BookingState.valueOf(state));
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllBookingsOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(defaultValue = "ALL")
                                                      @ValidState String state) {
        return bookingService.getAllBookingsOwner(userId, BookingState.valueOf(state));
    }
}
