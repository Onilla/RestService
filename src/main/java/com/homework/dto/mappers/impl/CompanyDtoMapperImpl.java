package com.homework.dto.mappers.impl;

import com.homework.dto.CompanyIncomingDto;
import com.homework.dto.CompanyOutGoingDto;
import com.homework.dto.CompanyUpdateDto;
import com.homework.dto.mappers.CompanyDtoMapper;
import com.homework.entity.Company;
import com.homework.entity.User;

import java.util.ArrayList;
import java.util.List;

public class CompanyDtoMapperImpl implements CompanyDtoMapper {

    @Override
    public Company map(CompanyIncomingDto companyIncomingDto) {
        return new Company(
                null,
                companyIncomingDto.getName()
        );
    }

    @Override
    public CompanyOutGoingDto map(Company company) {
        return new CompanyOutGoingDto(
                company.getId(),
                company.getName(),
                company.getUsers().stream().map(User::getLastname).toList());
    }
    @Override
    public Company map(CompanyUpdateDto companyUpdateDto) {
        return new Company(
                companyUpdateDto.getId(),
                companyUpdateDto.getName()
        );
    }
    public List<CompanyOutGoingDto> map(List<Company> companyList){
        List<CompanyOutGoingDto> companyOutGoingDtoList = new ArrayList<>();
        for (Company company : companyList) {
            companyOutGoingDtoList.add(map(company));
        }
        return companyOutGoingDtoList;
    }

}
