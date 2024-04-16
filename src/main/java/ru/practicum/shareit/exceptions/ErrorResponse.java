package ru.practicum.shareit.exceptions;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String message;
}
