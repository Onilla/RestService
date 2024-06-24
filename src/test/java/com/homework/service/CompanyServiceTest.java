package com.homework.service;

import com.homework.dto.CompanyIncomingDto;
import com.homework.dto.CompanyOutGoingDto;
import com.homework.dto.CompanyUpdateDto;
import com.homework.entity.Company;
import com.homework.exception.NotFoundException;
import com.homework.repository.impl.CompanyRepositoryImpl;
import com.homework.service.impl.CompanyServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(
        MockitoExtension.class
)
class CompanyServiceTest {
    @Mock
    private static CompanyRepositoryImpl mockRepository;

    @InjectMocks
    private CompanyServiceImpl service;

    @Test
    void delete() throws NotFoundException {
        Long id = 1L;
        Mockito.doReturn(true).when(mockRepository).existById(1L);
        service.delete(id);
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockRepository).deleteById(argumentCaptor.capture());
        Long result = argumentCaptor.getValue();
        Assertions.assertEquals(id, result);
    }

    @Test
    void saveCompany() {
        Long id = 1L;
        String companyName = "Yandex";
        Company company = new Company(id, companyName);
        CompanyIncomingDto companyIncomingDto = new CompanyIncomingDto();
        when(mockRepository.save(Mockito.any(Company.class))).thenReturn(company);
        CompanyOutGoingDto companyOutGoingDto = service.save(companyIncomingDto);
        Assertions.assertNotNull(companyOutGoingDto);
        Assertions.assertEquals(id, companyOutGoingDto.getId());
    }

    @Test
    void update() throws NotFoundException {
        Long id = 1L;
        String companyName = "Yandex";
        CompanyUpdateDto dto = new CompanyUpdateDto(id, companyName);
        Mockito.doReturn(true).when(mockRepository).existById(Mockito.any());
        service.update(dto);
        ArgumentCaptor<Company> argumentCaptor = ArgumentCaptor.forClass(Company.class);
        Mockito.verify(mockRepository).update(argumentCaptor.capture());
        Company companyCaptor = argumentCaptor.getValue();
        Assertions.assertEquals(id, companyCaptor.getId());
        Assertions.assertEquals(companyName, companyCaptor.getName());
    }

    @Test
    void findById() throws NotFoundException {
        Long id = 1L;
        String companyName = "Yandex";
        Company company = new Company(id, companyName);
        when(mockRepository.findById(id)).thenReturn(Optional.ofNullable(company));
        CompanyOutGoingDto savedCompany = service.findById(id);
        Assertions.assertNotNull(savedCompany);
        Assertions.assertEquals(companyName, savedCompany.getName());
    }

    @Test
    void findAll() {
        service.findAll();
        Mockito.verify(mockRepository).findAll();
    }

    @Test
    void findAllNotNull() {
        Company company = new Company(1L, "Yandex");
        Company company2 = new Company(2L, "Google");
        List<Company> list = new ArrayList<>();
        list.add(company);
        list.add(company2);
        when(mockRepository.findAll()).thenReturn(list);
        List<Company> list3 = mockRepository.findAll();
        Assertions.assertNotNull(list3);
    }

}
