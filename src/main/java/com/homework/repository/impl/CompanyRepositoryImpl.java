package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.entity.Company;
import com.homework.entity.User;
import com.homework.exception.DBException;
import com.homework.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyRepositoryImpl implements Repository<Company, Long> {

    private final ConnectionManager connectionManager = new ConnectionManagerImpl();

    private Company createCompany(ResultSet resultSet) throws SQLException {
        String findUsersByCompanyId = """
                SELECT user_id, user_firstname, user_lastname, u.company_id FROM users AS u
                INNER JOIN companies AS c ON u.company_id = c.company_id
                WHERE u.company_id = ?
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findUsers = connection.prepareStatement(findUsersByCompanyId)) {
            long companyId = resultSet.getLong("company_id");
            findUsers.setLong(1, companyId);
            List<User> users = new ArrayList<>();
            ResultSet resultUsers = findUsers.executeQuery();
            while (resultUsers.next()) {
                User user = new User(resultUsers.getLong("user_id"),
                        resultUsers.getString("user_lastname"));
                users.add(user);
            }
            return new Company(resultSet.getLong("company_id"),
                    resultSet.getString("company_name"), users);
        }
    }

    @Override
    public Optional<Company> findById(Long id) {
        Company company = null;
        String findCompanyById = """
                SELECT company_id, company_name FROM companies AS c
                WHERE c.company_id = ?
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findCompanyById)) {
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
        String deleteCompanyById = """
                DELETE FROM companies
                WHERE company_id = ? ;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteCompanyById)) {
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
        String findAllCompanies = """
                SELECT company_id, company_name FROM companies ;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllCompanies)) {
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
        String save = """
                INSERT INTO companies (company_name)
                VALUES (?) ;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveCompany = connection.prepareStatement(save, Statement.RETURN_GENERATED_KEYS)) {
            saveCompany.setString(1, company.getName());
            saveCompany.executeUpdate();
            ResultSet resultSet = saveCompany.getGeneratedKeys();
            if (resultSet.next()) {
                company.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return company;
    }

    @Override
    public void update(Company company) {
        String updateCompany = """
                UPDATE companies
                SET company_name = ?
                WHERE company_id = ?  ;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateCompany)) {
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
        String existCompanyById = """
                    SELECT exists (
                    SELECT 1 FROM companies
                    WHERE company_id = ?);
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(existCompanyById)) {
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
