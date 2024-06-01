package ru.practicum.shareit.user.dto;

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
public class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @DisplayName("Тест на корректную сериализацию объекта UserDto")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        UserDto userDto = UserDto.builder()
                .name("Inna")
                .email("inna@mail.ru")
                .build();

        JsonContent<UserDto> userDtoJson = this.json.write(userDto);

        assertThat(userDtoJson).hasJsonPathValue("$.name");
        assertThat(userDtoJson).extractingJsonPathStringValue("$.name").isEqualTo("Inna");

        assertThat(userDtoJson).hasJsonPathValue("$.email");
        assertThat(userDtoJson).extractingJsonPathStringValue("$.email").isEqualTo("inna@mail.ru");
    }

    @DisplayName("Тест на корректную десериализацию объекта UserDto")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        UserDto userDto = new UserDto(null, "Inna", "inna@mail.ru");

        var resource = new ClassPathResource("userDto.json");
        String content = Files.readString(resource.getFile().toPath());

        assertThat(this.json.parse(content)).isEqualTo(userDto);
    }
}