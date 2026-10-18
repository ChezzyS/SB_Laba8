package ru.arkhipov.lab8.service;

import ru.arkhipov.lab8.dto.UserDto;
import ru.arkhipov.lab8.entity.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
