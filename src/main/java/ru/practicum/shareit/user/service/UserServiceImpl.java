package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.user.dto.*;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import javax.validation.ValidationException;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            log.warn("User with id={} not found", userId);
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        log.info("The user was received by id={}", userId);
        return userMapper.toUserDto(user);
    }

    @Override
    public Collection<UserDto> getAllUserDto() {
        log.info("All users have been received");
        return userMapper.listToUserDto(userRepository.getAllUser());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User createdUser = userRepository.createUser(userMapper.toUser(userDto));
        if (createdUser == null) {
            log.warn("The user with this email={} already exists", userDto.getEmail());
            throw new ConflictException("The user with this email=" + userDto.getEmail() + " already exists");
        }
        log.info("User has been created={}", createdUser);
        return userMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDtoNew) {
        if (!userRepository.isExistId(userId)) {
            log.warn("The user with this id={} not already exists", userDtoNew.getId());
            throw new ValidationException("The user with this id=" + userDtoNew.getId() + " not already exists");
        }
        UserDto userDtoOld = getUserById(userId);
        isExistEmail(userDtoNew.getEmail(), userDtoOld.getEmail());
        setUser(userDtoOld, userDtoNew);
        User updatedUser = userRepository.updateUser(userMapper.toUser(userDtoOld));
        log.info("User has been updated={}", updatedUser);
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        log.info("User with id={} deleted", userId);
        userRepository.deleteUserById(userId);
    }

    private void isExistEmail(String emailNew, String email) {
        if (emailNew == null) {
            return;
        }
        boolean isExistEmail = userRepository.isExistEmail(emailNew);
        if (isExistEmail && !emailNew.equals(email)) {
            log.warn("The user with this email={} already exists", emailNew);
            throw new ConflictException("The user with this email=" + emailNew + " already exists");
        }
    }

    private void setUser(UserDto userDtoOld, UserDto userDtoNew) {
        if (userDtoNew.getName() != null && !userDtoNew.getName().isEmpty()) {
            userDtoOld.setName(userDtoNew.getName());
        }
        if (userDtoNew.getEmail() != null && !userDtoNew.getEmail().isEmpty()) {
            userDtoOld.setEmail(userDtoNew.getEmail());
        }
    }
}