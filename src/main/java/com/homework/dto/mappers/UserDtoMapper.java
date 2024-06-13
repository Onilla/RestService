package com.homework.dto.mappers;

import com.homework.dto.*;

import com.homework.entity.User;

import java.util.List;

public interface UserDtoMapper {
    User map(UserIncomingDto userIncomingDto);
    User map(UserUpdateDto userUpdateDto);

    UserOutGoingDto map(User user);
    List<UserOutGoingDto> map(List<User> userList);


}
