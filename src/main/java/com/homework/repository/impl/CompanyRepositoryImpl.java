package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.entity.Company;
import com.homework.exception.DBException;
import com.homework.repository.Repository;
import com.homework.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyRepositoryImpl implements Repository<Company, Long> {

    private final ConnectionManager connectionManager = new ConnectionManagerImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();

    private static Company createCompany(ResultSet resultSet) throws SQLException {
        return new Company(resultSet.getLong("company_id"),
                resultSet.getString("company_name"));
    }

    @Override
    public Optional<Company> findById(Long id) {
        Company company = null;
        String FIND_COMPANY_BY_ID = """
            SELECT company_id, company_name FROM companies
            WHERE company_id = ?
            """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_COMPANY_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                company = createCompany(resultSet);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return Optional.ofNullable(company);
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        String DELETE_COMPANY_BY_ID = """
            DELETE FROM companies
            WHERE company_id = ? ;
            """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMPANY_BY_ID)) {

            preparedStatement.setLong(1, id);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return deleteResult;
    }

    @Override
    public List<Company> findAll() {
        List<Company> companyList = new ArrayList<>();
        String FIND_ALL_COMPANIES = """
            SELECT company_id, company_name FROM companies ;
            """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_COMPANIES)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                companyList.add(createCompany(resultSet));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return companyList;
    }

    @Override
    public Company save(Company company) {
        String SAVE_COMPANY = """
            INSERT INTO companies (company_name)
            VALUES (?) ;
            """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_COMPANY, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, company.getName());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                company = new Company(
                        resultSet.getLong("company_id"),
                        company.getName(),userRepository.findByCompanyId(company.getId()));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return company;
    }

    @Override
    public void update(Company company) {
        String UPDATE_COMPANY = """
            UPDATE companies
            SET company_name = ?
            WHERE company_id = ?  ;
            """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMPANY)) {

            preparedStatement.setString(1, company.getName());
            preparedStatement.setLong(2, company.getId());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }


    @Override
    public boolean existById(Long id) {
        boolean isExists = false;
        String EXIST_COMPANY_BY_ID = """
                SELECT exists (
                SELECT 1 FROM companies
                WHERE company_id = ?);
            """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_COMPANY_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return isExists;
    }

}
