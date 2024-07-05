package com.homework.service.impl;

import com.homework.dto.UserIncomingDto;
import com.homework.dto.UserOutGoingDto;
import com.homework.dto.UserUpdateDto;
import com.homework.dto.mappers.UserDtoMapper;
import com.homework.dto.mappers.impl.UserDtoMapperImpl;
import com.homework.entity.User;
import com.homework.exception.NotFoundException;
import com.homework.fabric.Fabric;
import com.homework.repository.impl.UserRepositoryImpl;
import com.homework.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private UserRepositoryImpl userRepository;
    private UserDtoMapper dtoMapper;

    public UserServiceImpl(){
        this. userRepository = Fabric.getUserRepository();
        this.dtoMapper = Fabric.getUserDtoMapper();
    }

    @Override
    public UserOutGoingDto save(UserIncomingDto userIncomingDto) {
        User user = userRepository.save(dtoMapper.map(userIncomingDto));
        return dtoMapper.map(user);

    }

    @Override
    public UserOutGoingDto findById(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        return dtoMapper.map(user);
    }

    @Override
    public boolean delete(Long userId) throws NotFoundException {
        checkUserExistById(userId);
        return userRepository.deleteById(userId);
    }

    @Override
    public void update(UserUpdateDto userUpdateDto) throws NotFoundException {
        checkUserExistById(userUpdateDto.getId());
        userRepository.update(dtoMapper.map(userUpdateDto));
    }

    @Override
    public List<UserOutGoingDto> findAll() {
        List<User> userList = userRepository.findAll();
        return dtoMapper.map(userList);
    }

    private void checkUserExistById(Long userId) throws NotFoundException {
        if (!userRepository.existById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
