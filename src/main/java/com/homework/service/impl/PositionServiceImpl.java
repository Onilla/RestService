package com.homework.service.impl;

import com.homework.dto.PositionIncomingDto;
import com.homework.dto.PositionOutGoingDto;
import com.homework.dto.PositionUpdateDto;
import com.homework.dto.mappers.PositionDtoMapper;
import com.homework.dto.mappers.impl.PositionDtoMapperImpl;
import com.homework.entity.Position;
import com.homework.exception.NotFoundException;
import com.homework.repository.Repository;
import com.homework.repository.impl.PositionRepositoryImpl;
import com.homework.service.PositionService;

import java.util.List;

public class PositionServiceImpl implements PositionService {

    private final Repository<Position, Long> positionRepository = new PositionRepositoryImpl();
    private final PositionDtoMapper dtoMapper = new PositionDtoMapperImpl();

    @Override
    public PositionOutGoingDto save(PositionIncomingDto positionIncomingDto) {
        Position savedPosition = positionRepository.save(dtoMapper.map(positionIncomingDto));
        return dtoMapper.map(savedPosition);
    }

    @Override
    public PositionOutGoingDto findById(Long id) throws NotFoundException {
        Position position = positionRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Должность не найдена"));
        return dtoMapper.map(position);
    }

    @Override
    public boolean delete(Long id) throws NotFoundException {
        checkPositionExistById(id);
        return positionRepository.deleteById(id);
    }

    @Override
    public void update(PositionUpdateDto positionUpdateDto) throws NotFoundException {
        checkPositionExistById(positionUpdateDto.getId());
        positionRepository.update(dtoMapper.map(positionUpdateDto));
    }

    private void checkPositionExistById(Long positionId) throws NotFoundException {
        if (!positionRepository.existById(positionId)) {
            throw new NotFoundException("Должность не найдена");
        }
    }

    @Override
    public List<PositionOutGoingDto> findAll() {
        List<Position> positionList = positionRepository.findAll();
        return dtoMapper.map(positionList);
    }
}
