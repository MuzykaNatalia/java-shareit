package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public User getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        }
        return null;
    }

    @Override
    public Collection<User> getAllUser() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        if (isExistEmail(user.getEmail())) {
            return null;
        }

        user.setId(id++);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public boolean isExistId(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public boolean isExistEmail(String email) {
        if (users.isEmpty()) {
            return false;
        }

        User duplicate = users.values().stream()
                .filter(userDuplicate -> userDuplicate.getEmail().equals(email))
                .findFirst()
                .orElse(null);

        return duplicate != null;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void deleteUserById(Long userId) {
        users.remove(userId);
    }
}