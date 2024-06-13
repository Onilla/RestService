package com.homework.dto.mappers;

import com.homework.dto.*;
import com.homework.entity.Position;
import com.homework.entity.User;

import java.util.List;

public interface PositionDtoMapper {
    List<User> createUsersList(PositionIncomingDto positionIncomingDto);
    Position map(PositionIncomingDto positionIncomingDto);
    Position map(PositionUpdateDto positionUpdateDto);

    PositionOutGoingDto map(Position position);
    List<PositionOutGoingDto> map(List<Position> positions);
}
