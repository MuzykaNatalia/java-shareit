package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Constant.DATE_FORMAT;

@JsonTest
@AutoConfigureJsonTesters
public class ItemRequestDtoInfoTest {
    @Autowired
    private JacksonTester<ItemRequestDtoInfo> json;
    private final LocalDateTime current = LocalDateTime.parse("2024-05-19T21:09:45", DATE_FORMAT);

    @DisplayName("Тест на корректную сериализацию объекта ItemRequestDtoInfo")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        ItemRequestDtoInfo itemRequestDtoInfo = ItemRequestDtoInfo
                .builder()
                .description("need hoe")
                .created(current)
                .items(List.of(new ItemDto(null, "hoe", "garden hoe", true, null)))
                .build();

        assertThat(this.json.write(itemRequestDtoInfo)).hasJsonPathValue("$.description");
        assertThat(this.json.write(itemRequestDtoInfo)).extractingJsonPathStringValue("$.description")
                .isEqualTo("need hoe");

        assertThat(this.json.write(itemRequestDtoInfo)).hasJsonPathValue("$.created");
        assertThat(this.json.write(itemRequestDtoInfo)).extractingJsonPathStringValue("$.created")
                .isEqualTo(current.format(DATE_FORMAT));

        assertThat(this.json.write(itemRequestDtoInfo)).hasJsonPathValue("$.items");
        assertThat(this.json.write(itemRequestDtoInfo)).extractingJsonPathStringValue("$.items[0].name")
                .isEqualTo("hoe");
        assertThat(this.json.write(itemRequestDtoInfo)).extractingJsonPathStringValue("$.items[0].description")
                .isEqualTo("garden hoe");
        assertThat(this.json.write(itemRequestDtoInfo)).extractingJsonPathValue("$.items[0].available")
                .isEqualTo(true);
    }

    @DisplayName("Тест на корректную десериализацию объекта ItemRequestDtoInfo")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        ItemRequestDtoInfo itemRequestDtoInfo = new ItemRequestDtoInfo(null, "need hoe", current,
                List.of(new ItemDto(null, "hoe", "garden hoe", true, null)));

        String content = "{" +
                "\"description\":\"need hoe\"," +
                "\"created\":\"2024-05-19T21:09:45\"," +
                "\"items\":[{" +
                "\"name\":\"hoe\"," +
                "\"description\":\"garden hoe\"," +
                "\"available\":\"true\"," +
                "\"request\":\"null\"}]" +
                "}";

        assertThat(this.json.parse(content)).isEqualTo(itemRequestDtoInfo);
    }
}