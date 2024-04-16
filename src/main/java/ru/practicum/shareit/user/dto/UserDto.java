package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.config.*;
import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDto {
    @Positive(groups = Update.class)
    private Long id;
    @NotBlank(groups = Create.class)
    @Size(max = 50, groups = Create.class)
    private String name;
    @NotBlank(groups = Create.class)
    @Email(groups = {Create.class, Update.class})
    private String email;
}