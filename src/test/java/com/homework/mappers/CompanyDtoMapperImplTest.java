package com.homework.mappers;

import com.homework.dto.CompanyIncomingDto;
import com.homework.dto.CompanyOutGoingDto;
import com.homework.dto.CompanyUpdateDto;
import com.homework.dto.mappers.CompanyDtoMapper;
import com.homework.dto.mappers.impl.CompanyDtoMapperImpl;
import com.homework.entity.Company;
import com.homework.entity.User;
import com.homework.fabric.Fabric;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompanyDtoMapperImplTest {
    private CompanyDtoMapper mapper = Fabric.getDtoMapper();

    @Test
    void mapIncomingToCompany() {
        CompanyIncomingDto dto = new CompanyIncomingDto("Oracle");
        Company result = mapper.map(dto);

        Assertions.assertNull(result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @Test
    void mapCompanyToOutGoing() {
        Company company = new Company(10L, "Netflix", List.of(new User(), new User()));

        CompanyOutGoingDto result = mapper.map(company);

        Assertions.assertEquals(company.getId(), result.getId());
        Assertions.assertEquals(company.getName(), result.getName());
        Assertions.assertEquals(company.getUsers().size(), result.getUsers().size());
    }

    @Test
    void companyUpdateToCompany() {
        CompanyUpdateDto dto = new CompanyUpdateDto(5L, "New Company");

        Company result = mapper.map(dto);
        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @Test
    void listCompanyToListOutCompany() {
        List<Company> companyList = List.of(
                new Company(11L, "Oracle", List.of()),
                new Company(12L, "Netflix", List.of()),
                new Company(13L, "Epam", List.of())
        );

        List<CompanyOutGoingDto> result = mapper.map(companyList);

        Assertions.assertEquals(3, result.size());

    }
}