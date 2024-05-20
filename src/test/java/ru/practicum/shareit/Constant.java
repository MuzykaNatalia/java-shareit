package ru.practicum.shareit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constant {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final String HEADER_USER = "X-Sharer-User-Id";
    public static final LocalDateTime TIME_NOW = LocalDateTime.now();
}
