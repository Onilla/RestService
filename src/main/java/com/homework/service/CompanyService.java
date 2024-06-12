package com.homework.service;

import com.homework.dto.CompanyIncomingDto;
import com.homework.dto.CompanyOutGoingDto;
import com.homework.dto.CompanyUpdateDto;
import com.homework.exception.NotFoundException;

import java.util.List;

public interface CompanyService {

    CompanyOutGoingDto save(CompanyIncomingDto companyIncomingDto);

    CompanyOutGoingDto findById(Long id) throws NotFoundException;

    boolean delete(Long id) throws NotFoundException;

    void update(CompanyUpdateDto companyUpdateDto) throws NotFoundException;

    List<CompanyOutGoingDto> findAll();

}
