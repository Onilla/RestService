package com.homework.dto.mappers;

import com.homework.dto.*;

import com.homework.entity.User;

import java.util.List;

public interface UserDtoMapper {
    User map(UserIncomingDto userIncomingDto);
//    User map(UserUpdateDto userUpdateDto);

    UserFullOutGoingDto map(User user);
    List<UserFullOutGoingDto> map(List<User> userList);

}
