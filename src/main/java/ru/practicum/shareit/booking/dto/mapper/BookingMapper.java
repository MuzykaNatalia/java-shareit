package ru.practicum.shareit.booking.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoInfo;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoCreate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import java.util.*;
import java.util.stream.Collectors;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@Component
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.toItemDto(booking.getItem()))
                .booker(userMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public Collection<BookingDto> toBookingDtoCollection(Collection<Booking> booking) {
        return booking.stream()
                .map(this::toBookingDto)
                .collect(Collectors.toList());
    }

    public Booking toBooking(BookingDtoCreate bookingDtoCreate, User user, Item item) {
        return Booking.builder()
                .start(bookingDtoCreate.getStart())
                .end(bookingDtoCreate.getEnd())
                .item(item)
                .booker(user)
                .status(WAITING)
                .build();
    }

    public BookingDtoInfo toBookingDtoInfo(Booking booking) {
        return BookingDtoInfo.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .itemId(booking.getItem().getId())
                .build();
    }

    public List<BookingDtoInfo> toBookingDtoInfoList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toBookingDtoInfo)
                .collect(Collectors.toList());
    }

    public Map<Long, BookingDtoInfo> toBookingDtoInfoMapByIdItem(List<BookingDtoInfo> booking) {
        return booking.stream().collect(Collectors.toMap(
                BookingDtoInfo::getItemId, bookingDtoInfo -> bookingDtoInfo));
    }
}
