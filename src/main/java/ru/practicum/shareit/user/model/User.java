package ru.practicum.shareit.user.model;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
}