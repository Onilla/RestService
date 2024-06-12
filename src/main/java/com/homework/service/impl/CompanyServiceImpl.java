package com.homework.service.impl;

import com.homework.dto.CompanyIncomingDto;
import com.homework.dto.CompanyOutGoingDto;
import com.homework.dto.CompanyUpdateDto;
import com.homework.dto.mappers.CompanyDtoMapper;
import com.homework.dto.mappers.impl.CompanyDtoMapperImpl;
import com.homework.entity.Company;
import com.homework.exception.NotFoundException;
import com.homework.repository.CompanyRepository;
import com.homework.repository.impl.CompanyRepositoryImpl;
import com.homework.service.CompanyService;

import java.util.List;

public class CompanyServiceImpl implements CompanyService {

    CompanyRepository companyRepository = new CompanyRepositoryImpl();
    private CompanyDtoMapper dtoMapper = new CompanyDtoMapperImpl();

    @Override
    public CompanyOutGoingDto save(CompanyIncomingDto companyIncomingDto) {
        Company company = dtoMapper.map(companyIncomingDto);
        Company savedCompany = companyRepository.save(company);
        return dtoMapper.map(savedCompany);
    }

    @Override
    public CompanyOutGoingDto findById(Long companyId) throws NotFoundException {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
                new NotFoundException("Компания не найдена"));
        return dtoMapper.map(company);
    }

    @Override
    public boolean delete(Long companyId) throws NotFoundException {
        checkCompanyExistById(companyId);
        return companyRepository.deleteById(companyId);
    }

    @Override
    public void update(CompanyUpdateDto companyUpdateDto) throws NotFoundException {
        Company company = dtoMapper.map(companyUpdateDto);
        checkCompanyExistById(company.getId());
        companyRepository.update(company);
    }

    @Override
    public List<CompanyOutGoingDto> findAll() {
        List<Company> companyList = companyRepository.findAll();
        return dtoMapper.map(companyList);
    }

    private void checkCompanyExistById(Long companyId) throws NotFoundException {
        if (!companyRepository.existById(companyId)) {
            throw new NotFoundException("Компания не найдена");
        }
    }



}



