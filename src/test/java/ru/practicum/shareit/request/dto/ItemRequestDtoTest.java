package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.io.ClassPathResource;
import javax.validation.ConstraintViolation;
import javax.validation.*;
import java.nio.file.Files;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureJsonTesters
public class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Тест на корректную сериализацию объекта ItemRequestDto")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("need hoe")
                .build();

        JsonContent<ItemRequestDto> itemRequestDtoJson = this.json.write(itemRequestDto);

        assertThat(itemRequestDtoJson).hasJsonPathValue("$.description");
        assertThat(itemRequestDtoJson).extractingJsonPathStringValue("$.description")
                .isEqualTo("need hoe");
    }

    @DisplayName("Тест на корректную десериализацию объекта ItemRequestDto")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        ItemRequestDto itemRequestDto = new ItemRequestDto("need hoe");

        var resource = new ClassPathResource("itemRequestDto.json");
        String content = Files.readString(resource.getFile().toPath());

        assertThat(this.json.parse(content)).isEqualTo(itemRequestDto);
    }

    @DisplayName("Проверка корректной валидации объекта ItemRequestDto при создании и обновлении")
    @Test
    public void shouldValidation() {
        ItemRequestDto itemRequestDto = new ItemRequestDto("");
        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemRequestDto);

        assertThat(violations).isNotEmpty();
    }
}