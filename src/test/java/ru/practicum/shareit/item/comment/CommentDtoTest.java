package ru.practicum.shareit.item.comment;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import javax.validation.*;
import java.time.*;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Constant.DATE_FORMAT;

@JsonTest
@AutoConfigureJsonTesters
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;
    private Validator validator;
    private final LocalDateTime current = LocalDateTime.parse("2024-05-19T21:09:45", DATE_FORMAT);


    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Тест на корректную сериализацию объекта CommentDto")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        CommentDto commentDto = CommentDto.builder()
                .text("ok")
                .authorName("Sofia")
                .created(current)
                .itemId(1L)
                .build();

        assertThat(this.json.write(commentDto)).hasJsonPathValue("$.text");
        assertThat(this.json.write(commentDto)).extractingJsonPathStringValue("$.text")
                .isEqualTo("ok");

        assertThat(this.json.write(commentDto)).hasJsonPathValue("$.authorName");
        assertThat(this.json.write(commentDto)).extractingJsonPathStringValue("$.authorName")
                .isEqualTo("Sofia");

        assertThat(this.json.write(commentDto)).hasJsonPathValue("$.created");
        assertThat(this.json.write(commentDto)).extractingJsonPathStringValue("$.created")
                .isEqualTo(current.format(DATE_FORMAT));

        assertThat(this.json.write(commentDto)).hasJsonPathValue("$.itemId");
        assertThat(this.json.write(commentDto)).extractingJsonPathValue("$.itemId")
                .isEqualTo(1);
    }

    @DisplayName("Тест на корректную десериализацию объекта CommentDto")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        CommentDto commentDto = new CommentDto(null, "ok", "Sofia", current, 1L);
        String content = "{" +
                "\"text\":\"ok\"," +
                "\"authorName\":\"Sofia\"," +
                "\"created\":\"2024-05-19T21:09:45Z\"," +
                "\"itemId\":\"1\"" +
                "}";

        assertThat(this.json.parse(content)).isEqualTo(commentDto);
    }

    @DisplayName("Проверка корректной валидации объекта CommentDto при создании и обновлении")
    @Test
    public void shouldValidation() {
        CommentDto commentDto = new CommentDto(null, "", null, null, null);
        Set<ConstraintViolation<CommentDto>> violations = validator.validate(commentDto);

        assertThat(violations).isNotEmpty();
    }
}