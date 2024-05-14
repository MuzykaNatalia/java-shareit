package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import javax.validation.ValidationException;
import java.util.Collection;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserDto getUserDtoById(Long userId) {
        User user = userRepository.findById(userId).stream().findFirst().orElseThrow(() -> {
            log.warn("User with id={} not found", userId);
            throw new NotFoundException("User with id=" + userId + " not found");
        });

        log.info("The user was received by id={}", userId);
        return userMapper.toUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<UserDto> getAllUserDto() {
        log.info("All users have been received");
        List<User> allUsers = userRepository.findAll();
        return userMapper.toUserDtoCollection(allUsers);
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        try {
            User createdUser = userRepository.save(userMapper.toUser(userDto));
            log.info("User has been created={}", createdUser);
            return userMapper.toUserDto(createdUser);
        } catch (Exception e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UserDto userDtoNew) {
        User userOld = userRepository.findById(userId).stream().findFirst().orElseThrow(() -> {
            log.warn("The user with this id={} not already exists", userId);
            throw new ValidationException("The user with this id=" + userId + " not already exists");
        });

        isExistEmail(userDtoNew.getEmail(), userOld.getEmail());
        User updatedUser = userRepository.save(setUser(userOld, userDtoNew));
        log.info("User has been updated={}", updatedUser);
        return userMapper.toUserDto(updatedUser);
    }

    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        log.info("User with id={} deleted", userId);
        userRepository.deleteById(userId);
    }

    private void isExistEmail(String emailNew, String emailOld) {
        if (emailNew == null) {
            return;
        }
        boolean isExistEmail = userRepository.existsByEmail(emailNew);
        if (isExistEmail && !emailNew.equals(emailOld)) {
            log.warn("The user with this email={} already exists", emailNew);
            throw new ConflictException("The user with this email=" + emailNew + " already exists");
        }
    }

    private User setUser(User userOld, UserDto userDtoNew) {
        if (userDtoNew.getName() != null && !userDtoNew.getName().isEmpty()) {
            userOld.setName(userDtoNew.getName());
        }
        if (userDtoNew.getEmail() != null && !userDtoNew.getEmail().isEmpty()) {
            userOld.setEmail(userDtoNew.getEmail());
        }
        return userOld;
    }
}
