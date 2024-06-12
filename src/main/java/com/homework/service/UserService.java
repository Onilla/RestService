package com.homework.service;

import com.homework.dto.UserIncomingDto;
import com.homework.dto.UserOutGoingDto;
import com.homework.dto.UserUpdateDto;
import com.homework.exception.NotFoundException;

import java.util.List;

public interface UserService {

    UserOutGoingDto save(UserIncomingDto userIncomingDto);

    UserOutGoingDto findById(Long id) throws NotFoundException;

    boolean delete(Long id) throws NotFoundException;

//    void update(UserUpdateDto userUpdateDto) throws NotFoundException;

    List<UserOutGoingDto> findAll();
    List<UserOutGoingDto> findByCompanyId(Long id) throws NotFoundException;
}
