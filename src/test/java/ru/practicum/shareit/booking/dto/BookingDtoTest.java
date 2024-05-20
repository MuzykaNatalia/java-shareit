package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Constant.DATE_FORMAT;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@JsonTest
@AutoConfigureJsonTesters
public class BookingDtoTest {
    @Autowired
    private JacksonTester<BookingDto> json;
    private final LocalDateTime current = LocalDateTime.parse("2024-05-19T21:09:45", DATE_FORMAT);

    @DisplayName("Тест на корректную сериализацию объекта BookingDto")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        BookingDto bookingDto = BookingDto.builder()
                .start(current)
                .end(current.plusDays(3))
                .status(WAITING)
                .booker(new UserDto(null, "Inna", "inna@mail.ru"))
                .item(new ItemDto(null, "hoe", "garden hoe", true, null))
                .build();

        assertThat(this.json.write(bookingDto)).hasJsonPathValue("$.start");
        assertThat(this.json.write(bookingDto)).extractingJsonPathStringValue("$.start")
                .isEqualTo(current.format(DATE_FORMAT));

        assertThat(this.json.write(bookingDto)).hasJsonPathValue("$.end");
        assertThat(this.json.write(bookingDto)).extractingJsonPathStringValue("$.end")
                .isEqualTo(current.plusDays(3).format(DATE_FORMAT));

        assertThat(this.json.write(bookingDto)).hasJsonPathValue("$.status");
        assertThat(this.json.write(bookingDto)).extractingJsonPathStringValue("$.status")
                .isEqualTo(String.valueOf(WAITING));

        assertThat(this.json.write(bookingDto)).hasJsonPathValue("$.booker");
        assertThat(this.json.write(bookingDto)).extractingJsonPathStringValue("$.booker.name")
                .isEqualTo("Inna");
        assertThat(this.json.write(bookingDto)).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo("inna@mail.ru");

        assertThat(this.json.write(bookingDto)).hasJsonPathValue("$.item");
        assertThat(this.json.write(bookingDto)).extractingJsonPathStringValue("$.item.name")
                .isEqualTo("hoe");
        assertThat(this.json.write(bookingDto)).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("garden hoe");
        assertThat(this.json.write(bookingDto)).extractingJsonPathValue("$.item.available")
                .isEqualTo(true);
    }

    @DisplayName("Тест на корректную десериализацию объекта BookingDto")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        BookingDto bookingDto = new BookingDto(1L, current, current.plusDays(3), WAITING,
                new UserDto(null, "Inna", "inna@mail.ru"),
                new ItemDto(null, "hoe", "garden hoe", true, null));

        String content = "{" +
                "\"start\":\"2024-05-19T21:09:45\"," +
                "\"end\":\"2024-05-22T21:09:45\"," +
                "\"status\":\"WAITING\"," +
                "\"booker\":{" +
                "\"name\":\"Inna\"," +
                "\"email\":\"inna@mail.ru\"}," +
                "\"item\":{" +
                "\"name\":\"hoe\"," +
                "\"description\":\"garden hoe\"," +
                "\"available\":\"true\"," +
                "\"request\":\"null\"}" +
                "}";

        assertThat(this.json.parse(content)).isEqualTo(bookingDto);
    }
}