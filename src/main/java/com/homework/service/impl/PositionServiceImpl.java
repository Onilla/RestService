package com.homework.service.impl;

import com.homework.dto.PositionIncomingDto;
import com.homework.dto.PositionOutGoingDto;
import com.homework.dto.PositionUpdateDto;
import com.homework.dto.mappers.PositionDtoMapper;
import com.homework.dto.mappers.impl.PositionDtoMapperImpl;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.exception.NotFoundException;
import com.homework.repository.PositionRepository;
import com.homework.repository.impl.PositionRepositoryImpl;
import com.homework.service.PositionService;

import java.util.List;

public class PositionServiceImpl implements PositionService {

    PositionRepository positionRepository = new PositionRepositoryImpl();
    private PositionDtoMapper dtoMapper = new PositionDtoMapperImpl();

    @Override
    public PositionOutGoingDto save(PositionIncomingDto positionIncomingDto) {
        Position savedPosition = positionRepository.save(dtoMapper.map(positionIncomingDto));
        return dtoMapper.map(savedPosition);
    }

    @Override
    public PositionOutGoingDto findById(Long id) throws NotFoundException {
        return null;
    }

    @Override
    public boolean delete(Long id) throws NotFoundException {
        return false;
    }

    @Override
    public void update(PositionUpdateDto positionUpdateDto) throws NotFoundException {

    }

    @Override
    public List<PositionOutGoingDto> findAll() {
        return null;
    }
}
