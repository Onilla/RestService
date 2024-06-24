package com.homework.dto.mappers.impl;

import com.homework.dto.*;
import com.homework.dto.mappers.UserDtoMapper;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.repository.Repository;
import com.homework.repository.impl.CompanyRepositoryImpl;
import com.homework.repository.impl.PositionRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDtoMapperImpl implements UserDtoMapper {

    private final Repository<Position, Long> positionRepository = new PositionRepositoryImpl();
    private final Repository<Company, Long> companyRepository = new CompanyRepositoryImpl();

    @Override
    public User map(UserIncomingDto userIncomingDto) {
        List<Optional<Position>> listPositions = userIncomingDto.getPositions()
                .stream()
                .map(positionRepository::findById)
                .toList();
        List<Position> positions = new ArrayList<>();
        for (Optional<Position> p : listPositions) {
            p.ifPresent(positions::add);
        }
        Company company = companyRepository.findById(userIncomingDto.getCompanyId()).orElse(null);
        return new User(
                null,
                userIncomingDto.getFirstname(),
                userIncomingDto.getLastname(),
                company, positions
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
                .map(positionRepository::findById)
                .toList();
        List<Position> positions = new ArrayList<>();
        for (Optional<Position> p : list) {
            p.ifPresent(positions::add);
        }
        Company company = companyRepository.findById(userUpdateDto.getCompanyId()).orElse(null);
        return new User(
                userUpdateDto.getId(),
                userUpdateDto.getFirstname(),
                userUpdateDto.getLastname(),
                company, positions
        );
    }

    @Override
    public List<UserOutGoingDto> map(List<User> userList) {
        List<UserOutGoingDto> userOutGoingDtoList = new ArrayList<>();
        for (User user : userList) {
            userOutGoingDtoList.add(map(user));
        }
        return userOutGoingDtoList;
    }
}
