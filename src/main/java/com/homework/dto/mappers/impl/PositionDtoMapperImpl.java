package com.homework.dto.mappers.impl;

import com.homework.dto.PositionIncomingDto;
import com.homework.dto.PositionOutGoingDto;
import com.homework.dto.PositionUpdateDto;
import com.homework.dto.mappers.PositionDtoMapper;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.fabric.Fabric;
import com.homework.repository.Repository;
import com.homework.repository.impl.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class PositionDtoMapperImpl implements PositionDtoMapper {
    Repository<User, Long> userRepository;

    public PositionDtoMapperImpl(UserRepositoryImpl userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public List<User> createUsersList(PositionIncomingDto positionIncomingDto) {
        List<Optional<User>> listUsers = positionIncomingDto.getUserIds()
                .stream()
                .map(id -> userRepository.findById(id))
                .toList();
        List<User> users = new ArrayList<>();
        for (Optional<User> p : listUsers) {
            p.ifPresent(users::add);
        }
        return users;
    }

    @Override
    public Position map(PositionIncomingDto positionIncomingDto) {

        return new Position(
                null,
                positionIncomingDto.getName(),
                createUsersList(positionIncomingDto)
        );
    }

    @Override
    public Position map(PositionUpdateDto positionUpdateDto) {
        List<Optional<User>> listUsers = positionUpdateDto.getUserIds()
                .stream()
                .map(id -> userRepository.findById(id))
                .toList();
        List<User> users = new ArrayList<>();
        for (Optional<User> p : listUsers) {
            p.ifPresent(users::add);
        }
        return new Position(
                positionUpdateDto.getId(),
                positionUpdateDto.getName(),
                users
        );
    }

    @Override
    public PositionOutGoingDto map(Position position) {

        return new PositionOutGoingDto(
                position.getId(),
                position.getName(),
                position.getUsers().stream().map(User::getLastname).toList()
        );
    }

    @Override
    public List<PositionOutGoingDto> map(List<Position> positions) {
        List<PositionOutGoingDto> positionOutGoingDtos = new ArrayList<>();
        for (Position position : positions) {
            positionOutGoingDtos.add(map(position));
        }
        return positionOutGoingDtos;
    }
}

