package com.homework.repository;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ContainerConnectionManager;
import com.homework.entity.Company;
import com.homework.repository.impl.CompanyRepositoryImpl;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class CompanyRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = TestUtil.testUtil();

    ConnectionManager connectionManager = new ContainerConnectionManager(postgres.getJdbcUrl(),
            postgres.getUsername(),postgres.getPassword());

    private final CompanyRepositoryImpl companyRepository = new CompanyRepositoryImpl(connectionManager);

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void findById() {
        long id = 1L;
        Company company = companyRepository.findById(id).get();
        Assertions.assertNotNull(company);
    }

    @Test
    void findAll() {
        int expectedSize = 3;
        int resultSize = companyRepository.findAll().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }

    @Test
    void save() {
        Company company = new Company(null, "DevCompany");
        Company savedCompany = companyRepository.save(company);
        Assertions.assertNotNull(savedCompany.getId());
    }

    @Test
    void delete() {
        Company company = new Company(null, "DevCompany");
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
    void update() {
        String nameForUpdate = "Aston_Dev";
        Company companyForUpdate = companyRepository.findById(1L).get();
        String nameBeforeUpdate = companyForUpdate.getName();
        companyForUpdate.setName(nameForUpdate);
        companyRepository.update(companyForUpdate);
        Company companyAfterUpdate = companyRepository.findById(1L).get();
        Assertions.assertNotEquals(nameForUpdate, nameBeforeUpdate);
        Assertions.assertEquals(nameForUpdate, companyAfterUpdate.getName());
    }

}
