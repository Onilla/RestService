package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.entity.Company;
import com.homework.entity.User;
import com.homework.exception.DBException;
import com.homework.fabric.Fabric;
import com.homework.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyRepositoryImpl implements Repository<Company, Long> {

    public static final String FIND_USERS_BY_COMPANY_ID = """
            SELECT user_id, user_firstname, user_lastname, u.company_id FROM users AS u
            INNER JOIN companies AS c ON u.company_id = c.company_id
            WHERE u.company_id = ?
            """;

    public static final String FIND_COMPANY_BY_ID = """
            SELECT company_id, company_name FROM companies AS c
            WHERE c.company_id = ?
            """;

    public static final String DELETE_COMPANY_BY_ID = """
            DELETE FROM companies
            WHERE company_id = ? ;
            """;

    public static final String FIND_ALL_COMPANIES = """
            SELECT company_id, company_name FROM companies ;
            """;

    public static final String SAVE = """
            INSERT INTO companies (company_name)
            VALUES (?) ;
            """;

    public static final String UPDATE_COMPANY = """
            UPDATE companies
            SET company_name = ?
            WHERE company_id = ?  ;
            """;

    public static final String EXIST_COMPANY_BY_ID = """
                SELECT exists (
                SELECT 1 FROM companies
                WHERE company_id = ?);
            """;

    private final ConnectionManager connectionManager;

    public CompanyRepositoryImpl() {
        this.connectionManager = Fabric.getConnectionManager();
    }

    public CompanyRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    private Company createCompany(ResultSet resultSet) throws SQLException {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findUsers = connection.prepareStatement(FIND_USERS_BY_COMPANY_ID)) {
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
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveCompany = connection.prepareStatement(SAVE, Statement.RETURN_GENERATED_KEYS)) {
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
