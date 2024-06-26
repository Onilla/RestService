package com.homework.repository;

import com.homework.entity.Company;
import com.homework.repository.impl.CompanyRepositoryImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class CompanyRepositoryTest {

    private final Repository<Company, Long> companyRepository = new CompanyRepositoryImpl();
    @Container
    private static final PostgreSQLContainer<?> postgres = TestUtil.testUtil();

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void saveCompany() {
        Long id = null;
        String companyName = "DevCompany";
        Company company = new Company(id, companyName);
        Company savedCompany = companyRepository.save(company);
        Assertions.assertNotNull(savedCompany.getId());
        companyRepository.deleteById(savedCompany.getId());
    }

    @Test
    void deleteCompany() {
        Long id = null;
        String companyName = "DevCompany";
        Company company = new Company(id, companyName);
        Company savedCompany = companyRepository.save(company);
        boolean deleteResult = companyRepository.deleteById(savedCompany.getId());
        Assertions.assertTrue(deleteResult);
    }

    @Test
    void existById() {
        boolean existResult = companyRepository.existById(1L);
        Assertions.assertTrue(existResult);
    }

    @Test
    void updateCompany() {
        String nameForUpdate = "Aston_Dev";
        Company companyForUpdate = companyRepository.findById(51L).get();
        String nameBeforeUpdate = companyForUpdate.getName();
        companyForUpdate.setName(nameForUpdate);
        companyRepository.update(companyForUpdate);
        Company companyAfterUpdate = companyRepository.findById(51L).get();
        Assertions.assertNotEquals(nameForUpdate, nameBeforeUpdate);
        Assertions.assertEquals(nameForUpdate, companyAfterUpdate.getName());
    }

    @Test
    void findById() {
        long id = 1L;
        Company company = companyRepository.findById(id).get();
        Assertions.assertNotNull(company);
    }

    @Test
    void findAllCompany() {
        int expectedSize = 13;
        int resultSize = companyRepository.findAll().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }

}
