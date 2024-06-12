package com.homework.service;

import com.homework.dto.PositionIncomingDto;
import com.homework.dto.PositionOutGoingDto;
import com.homework.dto.PositionUpdateDto;
import com.homework.exception.NotFoundException;

import java.util.List;

public interface PositionService {

    PositionOutGoingDto save(PositionIncomingDto positionIncomingDto);

    PositionOutGoingDto findById(Long id) throws NotFoundException;

    boolean delete(Long id) throws NotFoundException;

    void update(PositionUpdateDto positionUpdateDto) throws NotFoundException;

    List<PositionOutGoingDto> findAll();
}
