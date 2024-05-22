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
import static ru.practicum.shareit.Constant.HEADER_USER;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@Valid @RequestBody BookingDtoCreate bookingDtoCreate,
                                    @RequestHeader(HEADER_USER) Long userId) {
        return bookingService.createBooking(bookingDtoCreate, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateBooking(@RequestHeader(HEADER_USER) Long userId,
                                    @PathVariable @Positive @NotNull Long bookingId,
                                    @RequestParam @NotNull Boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getOneBookingUser(@PathVariable @Positive @NotNull Long bookingId,
                                        @RequestHeader(HEADER_USER) Long userId) {
        return bookingService.getOneBookingUser(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> getAllBookingsBooker(@RequestHeader(HEADER_USER) Long userId,
                                                       @RequestParam(defaultValue = "ALL") @ValidState String state,
                                                       @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                       @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return bookingService.getAllBookingsBooker(userId, BookingState.valueOf(state), from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllBookingsOwner(@RequestHeader(HEADER_USER) Long userId,
                                                      @RequestParam(defaultValue = "ALL") @ValidState String state,
                                                      @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                      @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return bookingService.getAllBookingsOwner(userId, BookingState.valueOf(state), from, size);
    }
}
