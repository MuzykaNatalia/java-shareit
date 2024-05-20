package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDtoInfo;
import ru.practicum.shareit.item.comment.CommentDto;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Constant.DATE_FORMAT;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@JsonTest
@AutoConfigureJsonTesters
public class ItemDtoInfoTest {
    @Autowired
    private JacksonTester<ItemDtoInfo> json;
    private final LocalDateTime current = LocalDateTime.parse("2024-05-19T21:09:45", DATE_FORMAT);

    @DisplayName("Тест на корректную сериализацию объекта ItemDtoInfo")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        ItemDtoInfo itemDtoInfo = ItemDtoInfo.builder()
                .name("hoe")
                .description("garden hoe")
                .available(true)
                .lastBooking(new BookingDtoInfo(null, 1L, current, current.plusDays(3),
                        WAITING, 1L))
                .nextBooking(null)
                .comments(List.of(new CommentDto(null, "ok", "Sofia", current, 1L)))
                .build();

        assertThat(this.json.write(itemDtoInfo)).hasJsonPathValue("$.name");
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathStringValue("$.name")
                .isEqualTo("hoe");

        assertThat(this.json.write(itemDtoInfo)).hasJsonPathValue("$.description");
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathStringValue("$.description")
                .isEqualTo("garden hoe");

        assertThat(this.json.write(itemDtoInfo)).hasJsonPathValue("$.available");
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(true);

        assertThat(this.json.write(itemDtoInfo)).hasJsonPathValue("$.lastBooking");
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathValue("$.lastBooking.bookerId")
                .isEqualTo(1);
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathValue("$.lastBooking.start")
                .isEqualTo(current.format(DATE_FORMAT));
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathValue("$.lastBooking.end")
                .isEqualTo(current.plusDays(3).format(DATE_FORMAT));
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathStringValue("$.lastBooking.status")
                .isEqualTo(String.valueOf(WAITING));
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathValue("$.lastBooking.itemId")
                .isEqualTo(1);

        assertThat(this.json.write(itemDtoInfo)).hasJsonPathValue("$.comments");
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathValue("$.comments[0].text")
                .isEqualTo("ok");
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathValue("$.comments[0].authorName")
                .isEqualTo("Sofia");
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathValue("$.comments[0].created")
                .isEqualTo(current.format(DATE_FORMAT));
        assertThat(this.json.write(itemDtoInfo)).extractingJsonPathValue("$.comments[0].itemId")
                .isEqualTo(1);
    }

    @DisplayName("Тест на корректную десериализацию объекта ItemDtoInfo")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        ItemDtoInfo itemDtoInfo = new ItemDtoInfo(null, "hoe", "garden hoe", true,
                new BookingDtoInfo(null, 1L, current, current.plusDays(3), WAITING, 1L),
                null, List.of(new CommentDto(null, "ok", "Sofia", current, 1L)));

        String content = "{" +
                "\"name\":\"hoe\"," +
                "\"description\":\"garden hoe\"," +
                "\"available\":\"true\"," +
                "\"lastBooking\":{" +
                "\"bookerId\":\"1\"," +
                "\"start\":\"2024-05-19T21:09:45\"," +
                "\"end\":\"2024-05-22T21:09:45\"," +
                "\"status\":\"WAITING\"," +
                "\"itemId\":\"1\"}," +
                "\"comments\":[{" +
                "\"text\":\"ok\"," +
                "\"authorName\":\"Sofia\"," +
                "\"created\":\"2024-05-19T21:09:45\"," +
                "\"itemId\":\"1\"}]" +
                "}";

        assertThat(this.json.parse(content)).isEqualTo(itemDtoInfo);
    }
}