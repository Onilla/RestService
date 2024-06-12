package com.homework.service.impl;

import com.homework.dto.UserIncomingDto;
import com.homework.dto.UserFullOutGoingDto;
import com.homework.dto.UserUpdateDto;
import com.homework.dto.mappers.UserDtoMapper;
import com.homework.dto.mappers.impl.UserDtoMapperImpl;
import com.homework.entity.User;
import com.homework.exception.NotFoundException;
import com.homework.repository.UserRepository;
import com.homework.repository.impl.UserRepositoryImpl;
import com.homework.service.UserService;

import java.util.List;


public class UserServiceImpl implements UserService {

    UserRepository userRepository = new UserRepositoryImpl();
    UserDtoMapper dtoMapper = new UserDtoMapperImpl();

    @Override
    public UserFullOutGoingDto save(UserIncomingDto userIncomingDto) {
        User user = userRepository.save(dtoMapper.map(userIncomingDto));
        return dtoMapper.map(userRepository.findById(user.getId()).orElse(user));

    }

    @Override
    public UserFullOutGoingDto findById(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        return dtoMapper.map(user);
    }

    @Override
    public boolean delete(Long userId) throws NotFoundException {
        checkUserExistById(userId);
        return userRepository.deleteById(userId);
    }

//    @Override
//    public void update(UserUpdateDto userUpdateDto) throws NotFoundException {
//        checkUserExistById(userUpdateDto.getId());
//        userRepository.update(dtoMapper.map(userUpdateDto));
//    }

    @Override
    public List<UserFullOutGoingDto> findAll() {
        List<User> userList = userRepository.findAll();
        return dtoMapper.map(userList);
    }

    @Override
    public List<UserFullOutGoingDto> findByCompanyId(Long companyId) throws NotFoundException{
        List<User> userList = userRepository.findByCompanyId(companyId);
        return dtoMapper.map(userList);
    }

    private void checkUserExistById(Long companyId) throws NotFoundException {
        if (!userRepository.existById(companyId)) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
