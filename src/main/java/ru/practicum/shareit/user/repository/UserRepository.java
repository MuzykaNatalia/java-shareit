package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    User getUserById(Long userId);

    Collection<User> getAllUser();

    User createUser(User user);

    boolean isExistId(Long userId);

    boolean isExistEmail(String email);

    User updateUser(User user);

    void deleteUserById(Long userId);
}