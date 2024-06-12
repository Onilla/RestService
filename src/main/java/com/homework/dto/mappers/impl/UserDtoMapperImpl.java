package com.homework.dto.mappers.impl;

import com.homework.dto.*;
import com.homework.dto.mappers.CompanyDtoMapper;
import com.homework.dto.mappers.PositionDtoMapper;
import com.homework.dto.mappers.UserDtoMapper;
import com.homework.entity.User;

import java.util.ArrayList;
import java.util.List;

public class UserDtoMapperImpl implements UserDtoMapper {

    CompanyDtoMapper companyDtoMapper = new CompanyDtoMapperImpl();
    PositionDtoMapper positionDtoMapper = new PositionDtoMapperImpl();
    @Override
    public User map(UserIncomingDto userIncomingDto) {
        return new User(
                null,
                userIncomingDto.getFirstname(),
                userIncomingDto.getLastname(),
                userIncomingDto.getCompany(), null
        );
    }

    @Override
    public UserFullOutGoingDto map(User user) {
        return new UserFullOutGoingDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                companyDtoMapper.map(user.getCompany()));

    }
//    @Override
//    public User map(UserUpdateDto userUpdateDto) {
//        return new User(
//                userUpdateDto.getId(),
//                userUpdateDto.getFirstname(),
//                userUpdateDto.getLastname(),
//                userUpdateDto.getCompany(),
//                positionDtoMapper.map(userUpdateDto.getPositionUpdateDtoList())
//        );
//    }
    public List<UserFullOutGoingDto> map(List<User> userList){
        List<UserFullOutGoingDto> userFullOutGoingDtoList = new ArrayList<>();
        for (User user : userList) {
            userFullOutGoingDtoList.add(map(user));
        }
        return userFullOutGoingDtoList;
    }
}
