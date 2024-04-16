package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.config.Create;
import ru.practicum.shareit.config.Update;
import javax.validation.constraints.*;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ItemDto {
    @Positive(groups = Update.class)
    private Long id;
    @NotBlank(groups = Create.class)
    @Size(max = 50, groups = Create.class)
    private String name;
    @NotBlank(groups = Create.class)
    @Size(max = 200, groups = Create.class)
    private String description;
    @NotNull(groups = Create.class)
    private Boolean available;
    private Long ownerId;
}