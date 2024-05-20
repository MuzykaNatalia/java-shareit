package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.config.*;
import javax.validation.ConstraintViolation;
import javax.validation.*;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@AutoConfigureJsonTesters
public class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Тест на корректную сериализацию объекта ItemDto")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        ItemDto itemDto = ItemDto.builder()
                .name("hoe")
                .description("garden hoe")
                .available(true)
                .build();

        assertThat(this.json.write(itemDto)).hasJsonPathValue("$.name");
        assertThat(this.json.write(itemDto)).extractingJsonPathStringValue("$.name")
                .isEqualTo("hoe");

        assertThat(this.json.write(itemDto)).hasJsonPathValue("$.description");
        assertThat(this.json.write(itemDto)).extractingJsonPathStringValue("$.description")
                .isEqualTo("garden hoe");

        assertThat(this.json.write(itemDto)).hasJsonPathValue("$.available");
        assertThat(this.json.write(itemDto)).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(true);
    }

    @DisplayName("Тест на корректную десериализацию объекта ItemDto")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        ItemDto itemDto = new ItemDto(null, "hoe", "garden hoe", true, null);
        String content = "{" +
                "\"name\":\"hoe\"," +
                "\"description\":\"garden hoe\"," +
                "\"available\":\"true\"" +
                "}";

        assertThat(this.json.parse(content)).isEqualTo(itemDto);
    }

    @DisplayName("Проверка корректной валидации объекта ItemDto при создании и обновлении")
    @Test
    public void shouldValidation() {
        ItemDto itemDto = new ItemDto(0L, "", "", null, 0L);
        ItemDto itemDtoTwo = new ItemDto(-1L, "hoe", "garden hoe", true, 1L);

        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto, Create.class);
        Set<ConstraintViolation<ItemDto>> violationsTwo = validator.validate(itemDtoTwo, Update.class);

        assertThat(violations).isNotEmpty();
        assertThat(violationsTwo).isNotEmpty();
    }
}