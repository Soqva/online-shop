package com.s0qva.application.service;

import com.s0qva.application.dto.order.OrderReadingDto;
import com.s0qva.application.dto.user.UserCreationDto;
import com.s0qva.application.dto.user.UserIdDto;
import com.s0qva.application.dto.user.UserReadingDto;
import com.s0qva.application.exception.NoSuchUserException;
import com.s0qva.application.exception.UserAlreadyExistsException;
import com.s0qva.application.exception.model.enumeration.DefaultExceptionMessage;
import com.s0qva.application.model.User;
import com.s0qva.application.repository.UserRepository;
import com.s0qva.application.mapper.user.GeneralUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final GeneralUserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, GeneralUserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserReadingDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapUserToUserReadingDto)
                .collect(Collectors.toList());
    }

    public UserReadingDto getUserById(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);

        return maybeUser.map(userMapper::mapUserToUserReadingDto)
                .orElseThrow(() ->
                        new NoSuchUserException(DefaultExceptionMessage.NO_SUCH_USER_WITH_ID.getMessage() + id));
    }

    public List<OrderReadingDto> getAllOrdersByUserId(Long id) {
        UserReadingDto userReadingDto = getUserById(id);
        return userReadingDto.getOrders();
    }

    public UserIdDto saveUser(UserCreationDto userCreationDto) {
        User user = userMapper.mapUserCreationDtoToUser(userCreationDto);

        Optional<User> maybeExistingUser = userRepository.findByUsername(user.getUsername());

        if (maybeExistingUser.isPresent()) {
            throw new UserAlreadyExistsException(DefaultExceptionMessage.USER_ALREADY_EXISTS_WITH_USERNAME.getMessage()
                    + user.getUsername());
        }

        User savedUser = userRepository.save(user);

        return userMapper.mapUserToUserIdDto(savedUser);
    }

    public UserReadingDto updateUser(Long id, UserCreationDto userCreationDto) {
        Optional<User> maybeOldUser = userRepository.findById(id);
        User oldUser = maybeOldUser.orElseThrow(() ->
                new NoSuchUserException(DefaultExceptionMessage.NO_SUCH_USER_WITH_ID.getMessage() + id));
        User newUser = userMapper.mapUserCreationDtoToUser(userCreationDto);

        oldUser.setUsername(newUser.getUsername());
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setRole(newUser.getRole());
        oldUser.setBanned(newUser.isBanned());

        User updatedUser = userRepository.save(oldUser);

        return userMapper.mapUserToUserReadingDto(updatedUser);
    }

    public void deleteUser(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
        User user = maybeUser.orElseThrow(() ->
                new NoSuchUserException(DefaultExceptionMessage.NO_SUCH_USER_WITH_ID.getMessage() + id));

        userRepository.delete(user);
    }
}
