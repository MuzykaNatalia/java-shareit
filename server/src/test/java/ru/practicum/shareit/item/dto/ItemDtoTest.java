package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.io.ClassPathResource;
import java.nio.file.Files;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureJsonTesters
public class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @DisplayName("Тест на корректную сериализацию объекта ItemDto")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        ItemDto itemDto = ItemDto.builder()
                .name("hoe")
                .description("garden hoe")
                .available(true)
                .build();

        JsonContent<ItemDto> itemDtoJson = this.json.write(itemDto);

        assertThat(itemDtoJson).hasJsonPathValue("$.name");
        assertThat(itemDtoJson).extractingJsonPathStringValue("$.name").isEqualTo("hoe");

        assertThat(itemDtoJson).hasJsonPathValue("$.description");
        assertThat(itemDtoJson).extractingJsonPathStringValue("$.description").isEqualTo("garden hoe");

        assertThat(itemDtoJson).hasJsonPathValue("$.available");
        assertThat(itemDtoJson).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }

    @DisplayName("Тест на корректную десериализацию объекта ItemDto")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        ItemDto itemDto = new ItemDto(null, "hoe", "garden hoe", true, null);

        var resource = new ClassPathResource("itemDto.json");
        String content = Files.readString(resource.getFile().toPath());

        assertThat(this.json.parse(content)).isEqualTo(itemDto);
    }
}