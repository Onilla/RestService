package com.homework.dto.mappers.impl;

import com.homework.dto.*;
import com.homework.dto.mappers.CompanyDtoMapper;
import com.homework.dto.mappers.PositionDtoMapper;
import com.homework.dto.mappers.UserDtoMapper;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.repository.CompanyRepository;
import com.homework.repository.PositionRepository;
import com.homework.repository.impl.CompanyRepositoryImpl;
import com.homework.repository.impl.PositionRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDtoMapperImpl implements UserDtoMapper {

    PositionRepository positionRepository = new PositionRepositoryImpl();
    CompanyRepository companyRepository = new CompanyRepositoryImpl();
    @Override
    public User map(UserIncomingDto userIncomingDto) {
        List<Optional<Position>> listPositions = userIncomingDto.getPositions()
                .stream()
                .map(id -> positionRepository.findById(id))
                .collect(Collectors.toList());
        List<Position> positions = new ArrayList<>();
        for (Optional<Position> p:listPositions){
            if (p.isPresent()) {
                positions.add(p.get());
            }
        }
        Company company = companyRepository.findById(userIncomingDto.getCompanyId()).orElse(null);
        return new User(
                null,
                userIncomingDto.getFirstname(),
                userIncomingDto.getLastname(),
                company,positions
                );
    }

    @Override
    public UserOutGoingDto map(User user) {
        return new UserOutGoingDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getCompany().getName(),
                user.getPositions().stream().map(Position::getName).toList()
                );

    }
    @Override
    public User map(UserUpdateDto userUpdateDto) {
        List<Optional<Position>> list = userUpdateDto.getPositions()
                .stream()
                .map(id -> positionRepository.findById(id))
                .collect(Collectors.toList());
        List<Position> positions = new ArrayList<>();
        for (Optional<Position> p:list){
            if (p.isPresent()) {
                positions.add(p.get());
            }
        }
        Company company = companyRepository.findById(userUpdateDto.getCompanyId()).orElse(null);
        return new User(
                userUpdateDto.getId(),
                userUpdateDto.getFirstname(),
                userUpdateDto.getLastname(),
                company,positions
        );
    }
    @Override
    public List<UserOutGoingDto> map(List<User> userList){
        List<UserOutGoingDto> userOutGoingDtoList = new ArrayList<>();
        for (User user : userList) {
            userOutGoingDtoList.add(map(user));
        }
        return userOutGoingDtoList;
    }
}
