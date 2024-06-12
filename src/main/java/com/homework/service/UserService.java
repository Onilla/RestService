package com.homework.service;

import com.homework.dto.UserIncomingDto;
import com.homework.dto.UserFullOutGoingDto;
import com.homework.dto.UserUpdateDto;
import com.homework.exception.NotFoundException;

import java.util.List;

public interface UserService {

    UserFullOutGoingDto save(UserIncomingDto userIncomingDto);

    UserFullOutGoingDto findById(Long id) throws NotFoundException;

    boolean delete(Long id) throws NotFoundException;

//    void update(UserUpdateDto userUpdateDto) throws NotFoundException;

    List<UserFullOutGoingDto> findAll();
    List<UserFullOutGoingDto> findByCompanyId(Long id) throws NotFoundException;
}
