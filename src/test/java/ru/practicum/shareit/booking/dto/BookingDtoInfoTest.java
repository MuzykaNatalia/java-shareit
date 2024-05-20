package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Constant.DATE_FORMAT;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@JsonTest
@AutoConfigureJsonTesters
public class BookingDtoInfoTest {
    @Autowired
    private JacksonTester<BookingDtoInfo> json;
    private final LocalDateTime current = LocalDateTime.parse("2024-05-19T21:09:45", DATE_FORMAT);

    @DisplayName("Тест на корректную сериализацию объекта BookingDtoInfo")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        BookingDtoInfo bookingDtoInfo = BookingDtoInfo.builder()
                .bookerId(1L)
                .start(current)
                .end(current.plusDays(3))
                .status(WAITING)
                .itemId(1L)
                .build();

        assertThat(this.json.write(bookingDtoInfo)).hasJsonPathValue("$.bookerId");
        assertThat(this.json.write(bookingDtoInfo)).extractingJsonPathValue("$.bookerId")
                .isEqualTo(1);

        assertThat(this.json.write(bookingDtoInfo)).hasJsonPathValue("$.start");
        assertThat(this.json.write(bookingDtoInfo)).extractingJsonPathStringValue("$.start")
                .isEqualTo(current.format(DATE_FORMAT));

        assertThat(this.json.write(bookingDtoInfo)).hasJsonPathValue("$.end");
        assertThat(this.json.write(bookingDtoInfo)).extractingJsonPathStringValue("$.end")
                .isEqualTo(current.plusDays(3).format(DATE_FORMAT));

        assertThat(this.json.write(bookingDtoInfo)).hasJsonPathValue("$.status");
        assertThat(this.json.write(bookingDtoInfo)).extractingJsonPathStringValue("$.status")
                .isEqualTo(String.valueOf(WAITING));

        assertThat(this.json.write(bookingDtoInfo)).hasJsonPathValue("$.itemId");
        assertThat(this.json.write(bookingDtoInfo)).extractingJsonPathValue("$.itemId")
                .isEqualTo(1);
    }

    @DisplayName("Тест на корректную десериализацию объекта BookingDtoInfo")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        BookingDtoInfo bookingDtoInfo = new BookingDtoInfo(null, 1L, current, current.plusDays(3),
                WAITING, 1L);

        String content = "{" +
                "\"bookerId\":\"1\"," +
                "\"start\":\"2024-05-19T21:09:45\"," +
                "\"end\":\"2024-05-22T21:09:45\"," +
                "\"status\":\"WAITING\"," +
                "\"itemId\":\"1\"" +
                "}";

        assertThat(this.json.parse(content)).isEqualTo(bookingDtoInfo);
    }
}