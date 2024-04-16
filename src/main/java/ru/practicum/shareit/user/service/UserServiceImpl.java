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
    private UserRepository repository;
    @Autowired
    private UserMapper mapper;

    @Override
    public UserDto getUserById(Long userId) {
        User user = repository.getUserById(userId);
        if (user == null) {
            log.warn("User with id=" + userId + " not found");
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        return mapper.toUserDto(user);
    }

    @Override
    public Collection<UserDto> getAllUserDto() {
        return mapper.listToUserDto(repository.getAllUser());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User createdUser = repository.createUser(mapper.toUser(userDto));
        if (createdUser == null) {
            log.warn("The user with this email=" + userDto.getEmail() + " already exists");
            throw new ConflictException("The user with this email=" + userDto.getEmail() + " already exists");
        }
        return mapper.toUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDtoNew) {
        if (!repository.isExistId(userId)) {
            log.warn("The user with this id=" + userDtoNew.getId() + " not already exists");
            throw new ValidationException("The user with this id=" + userDtoNew.getId() + " not already exists");
        }
        UserDto userDtoOld = getUserById(userId);
        isExistEmail(userDtoNew.getEmail(), userDtoOld.getEmail());
        setUser(userDtoOld, userDtoNew);
        User user = repository.updateUser(mapper.toUser(userDtoOld));
        return mapper.toUserDto(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        repository.deleteUserById(userId);
    }

    private void isExistEmail(String emailNew, String email) {
        if (emailNew == null) {
            return;
        }
        boolean isExistEmail = repository.isExistEmail(emailNew);
        if (isExistEmail && !emailNew.equals(email)) {
            log.warn("The user with this email=" + emailNew + " already exists");
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