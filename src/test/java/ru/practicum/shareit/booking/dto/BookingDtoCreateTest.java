package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Constant.DATE_FORMAT;

@JsonTest
@AutoConfigureJsonTesters
public class BookingDtoCreateTest {
    @Autowired
    private JacksonTester<BookingDtoCreate> json;
    private final LocalDateTime current = LocalDateTime.parse("2024-05-19T21:09:45", DATE_FORMAT);

    @DisplayName("Тест на корректную сериализацию объекта BookingDtoCreate")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        BookingDtoCreate bookingDtoCreate = BookingDtoCreate.builder()
                .itemId(1L)
                .start(current)
                .end(current.plusDays(3))
                .build();

        assertThat(this.json.write(bookingDtoCreate)).hasJsonPathValue("$.itemId");
        assertThat(this.json.write(bookingDtoCreate)).extractingJsonPathValue("$.itemId")
                .isEqualTo(1);

        assertThat(this.json.write(bookingDtoCreate)).hasJsonPathValue("$.start");
        assertThat(this.json.write(bookingDtoCreate)).extractingJsonPathStringValue("$.start")
                .isEqualTo(current.format(DATE_FORMAT));

        assertThat(this.json.write(bookingDtoCreate)).hasJsonPathValue("$.end");
        assertThat(this.json.write(bookingDtoCreate)).extractingJsonPathStringValue("$.end")
                .isEqualTo(current.plusDays(3).format(DATE_FORMAT));
    }

    @DisplayName("Тест на корректную десериализацию объекта BookingDtoCreate")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        BookingDtoCreate bookingDtoCreate = new BookingDtoCreate(1L, current, current.plusDays(3));

        String content = "{" +
                "\"itemId\":\"1\"," +
                "\"start\":\"2024-05-19T21:09:45\"," +
                "\"end\":\"2024-05-22T21:09:45\"" +
                "}";

        assertThat(this.json.parse(content)).isEqualTo(bookingDtoCreate);
    }
}