package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import javax.validation.constraints.*;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable @Positive @NotNull Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public Collection<UserDto> getAllUser(@RequestParam(defaultValue = "0", required = false) @Min(0) Integer from,
                                             @RequestParam(defaultValue = "10", required = false) @Min(1) Integer size) {
        return userService.getAllUser(from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable @Positive @NotNull Long userId,
                              @Validated(Update.class) @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable @Positive @NotNull Long userId) {
        userService.deleteUserById(userId);
    }
}
