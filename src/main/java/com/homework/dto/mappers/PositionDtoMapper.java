package com.homework.dto.mappers;

import com.homework.dto.*;
import com.homework.entity.Company;
import com.homework.entity.Position;

import java.util.List;

public interface PositionDtoMapper {

    Position map(PositionIncomingDto positionIncomingDto);
    Position map(PositionUpdateDto positionUpdateDto);

    PositionOutGoingDto map(Position position);
    List<PositionOutGoingDto> map(List<Position> positions);
}
