package com.homework.dto.mappers.impl;

import com.homework.dto.PositionIncomingDto;
import com.homework.dto.PositionOutGoingDto;
import com.homework.dto.PositionUpdateDto;
import com.homework.dto.UserOutGoingDto;
import com.homework.dto.mappers.PositionDtoMapper;
import com.homework.entity.Company;
import com.homework.entity.Position;

import java.util.List;

public class PositionDtoMapperImpl implements PositionDtoMapper {
    @Override
    public Position map(PositionIncomingDto positionIncomingDto) {
        return new Position(
                null,
                positionIncomingDto.getName(),
                null
        );
    }
//TODO map
    @Override
    public Position map(PositionUpdateDto positionUpdateDto) {
        return null;
    }

    @Override
    public PositionOutGoingDto map(Position position) {
        List<UserOutGoingDto> userList = position.getUsers()
                .stream().map(user -> new UserOutGoingDto(
                        user.getId(),
                        user.getFirstname(),
                        user.getLastname()
                )).toList();

        return new PositionOutGoingDto(
                position.getId(),
                position.getName(),
                userList
        );
    }

    @Override
    public List<PositionOutGoingDto> map(List<Position> positions) {
        return null;
    }
}
