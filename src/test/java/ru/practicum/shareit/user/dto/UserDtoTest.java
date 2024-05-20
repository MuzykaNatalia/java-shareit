package ru.practicum.shareit.user.dto;

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
public class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Тест на корректную сериализацию объекта UserDto")
    @Test
    @SneakyThrows
    public void shouldSerialize() {
        UserDto userDto = UserDto.builder()
                .name("Inna")
                .email("inna@mail.ru")
                .build();

        assertThat(this.json.write(userDto)).hasJsonPathValue("$.name");
        assertThat(this.json.write(userDto)).extractingJsonPathStringValue("$.name")
                .isEqualTo("Inna");

        assertThat(this.json.write(userDto)).hasJsonPathValue("$.email");
        assertThat(this.json.write(userDto)).extractingJsonPathStringValue("$.email")
                .isEqualTo("inna@mail.ru");
    }

    @DisplayName("Тест на корректную десериализацию объекта UserDto")
    @Test
    @SneakyThrows
    public void shouldDeserialize() {
        UserDto userDto = new UserDto(null, "Inna", "inna@mail.ru");
        String content = "{" +
                "\"name\":\"Inna\"," +
                "\"email\":\"inna@mail.ru\"" +
                "}";

        assertThat(this.json.parse(content)).isEqualTo(userDto);
    }

    @DisplayName("Проверка корректной валидации объекта UserDto при создании и обновлении")
    @Test
    public void shouldValidation() {
        UserDto userDto = new UserDto(null, "", "");
        UserDto userDtoTwo = new UserDto(null, "Inna", "kjl");

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto, Create.class);
        Set<ConstraintViolation<UserDto>> violationsTwo = validator.validate(userDtoTwo, Update.class);

        assertThat(violations).isNotEmpty();
        assertThat(violationsTwo).isNotEmpty();
    }
}