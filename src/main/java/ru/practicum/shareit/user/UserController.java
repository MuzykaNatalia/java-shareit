package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.config.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import javax.validation.constraints.*;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable @Positive @NotNull Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public Collection<UserDto> getAllUserDto() {
        return userService.getAllUserDto();
    }

    @PostMapping
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