package ru.practicum.shareit.item.comment;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.core.io.ClassPathResource;
import javax.validation.*;
import java.nio.file.Files;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Constant.DATE_FORMAT;
import static ru.practicum.shareit.Constant.FIXED_TIME;

@JsonTest
@AutoConfigureJsonTesters
public class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;
    private Validator validator;

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
                .created(FIXED_TIME)
                .itemId(1L)
                .build();

        JsonContent<CommentDto> commentDtoJson = this.json.write(commentDto);

        assertThat(commentDtoJson).hasJsonPathValue("$.text");
        assertThat(commentDtoJson).extractingJsonPathStringValue("$.text").isEqualTo("ok");

        assertThat(commentDtoJson).hasJsonPathValue("$.authorName");
        assertThat(commentDtoJson).extractingJsonPathStringValue("$.authorName").isEqualTo("Sofia");

        assertThat(commentDtoJson).hasJsonPathValue("$.created");
        assertThat(commentDtoJson).extractingJsonPathStringValue("$.created")
                .isEqualTo(FIXED_TIME.format(DATE_FORMAT));

        assertThat(commentDtoJson).hasJsonPathValue("$.itemId");
        assertThat(commentDtoJson).extractingJsonPathValue("$.itemId").isEqualTo(1);
    }

    @DisplayName("Тест на корректную десериализацию объекта CommentDto")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        CommentDto commentDto = new CommentDto(null, "ok", "Sofia", FIXED_TIME, 1L);

        var resource = new ClassPathResource("commentDto.json");
        String content = Files.readString(resource.getFile().toPath());

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