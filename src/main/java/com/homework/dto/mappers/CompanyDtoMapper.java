package com.homework.dto.mappers;

import com.homework.dto.CompanyIncomingDto;
import com.homework.dto.CompanyOutGoingDto;
import com.homework.dto.CompanyUpdateDto;
import com.homework.entity.Company;

import java.util.List;

public interface CompanyDtoMapper {

    Company map(CompanyIncomingDto companyIncomingDto);

    Company map(CompanyUpdateDto companyUpdateDto);

    CompanyOutGoingDto map(Company company);

    List<CompanyOutGoingDto> map(List<Company> companyList);

}
