package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.entity.Company;
import com.homework.entity.User;
import com.homework.repository.CompanyRepository;
import com.homework.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final String SAVE_USER = """
            INSERT INTO users (user_firstname, user_lastname, company_id)
            VALUES (?, ? ,?) ;
            """;

    private final String FIND_USER_BY_ID = """
            SELECT user_id, user_firstname, user_lastname, company_id FROM users
            WHERE user_id = ?;
            """;

    private final String FIND_USER_BY_COMPANY_ID = """
            SELECT user_id, user_firstname, user_lastname, company_id FROM users
            WHERE company_id = ?;
            """;
    private final String FIND_USERS_BY_POSITION_ID = """
            SELECT users_positions_id, user_id, position_id FROM users_positions
            WHERE position_id = ?;
            """;

    private final String DELETE_USER_BY_ID = """
            DELETE FROM users
            WHERE user_id = ? ;
            """;
    private final String EXIST_USER_BY_ID = """
                SELECT exists (
                SELECT 1 FROM users
                WHERE user_id = ?);
            """;

    private final String UPDATE_USER = """
            UPDATE users
            SET user_firstname = ?, user_lastname = ?, company_id = ?
            WHERE user_id = ?  ;
            """;

    private final String FIND_ALL_USERS = """
            SELECT user_id, user_firstname, user_lastname, company_id FROM users ;
            """;

    private ConnectionManager connectionManager = new ConnectionManagerImpl();
    private CompanyRepository companyRepository = new CompanyRepositoryImpl();


    private User createUser(ResultSet resultSet) throws SQLException {

        Long userId = resultSet.getLong("user_id");
        Company company = companyRepository.findById(resultSet.getLong("company_id")).orElse(null);
        return new User(userId,
                resultSet.getString("user_firstname"),
                resultSet.getString("user_lastname"),
                company, null);

    }

    @Override
    public Optional<User> findById(Long id) {
        User user = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = createUser(resultSet);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return Optional.ofNullable(user);
    }
    @Override
    public List<User> findByCompanyId(Long id) {
        List<User> usersList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_COMPANY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                usersList.add(createUser(resultSet));
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return usersList;
    }
    @Override
    public List<User> findUsersByPositionId(Long positionId) {
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USERS_BY_POSITION_ID)) {

            preparedStatement.setLong(1, positionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long userId = resultSet.getLong("user_id");
                Optional<User> optionalUser = findById(userId);
                if (optionalUser.isPresent()) {
                    users.add(optionalUser.get());
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return users;
    }

    @Override
    public List<User> findAll() {
        List<User> usersList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                usersList.add(createUser(resultSet));
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return usersList;
    }

    @Override
    public User save(User user) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            if (user.getCompany() == null) {
                preparedStatement.setNull(3, Types.NULL);
            } else {
                preparedStatement.setLong(3, user.getCompany().getId());
            }
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getLong("user_id"),
                        user.getFirstname(),
                        user.getLastname(),
                        user.getCompany(),null);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка сохранения в базу данных");
        }
        return user;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID);) {

            preparedStatement.setLong(1, id);

            deleteResult = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.getMessage();
        }
        return deleteResult;
    }


    @Override
    public void update(User user) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);) {

            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            if (user.getCompany() == null) {
                preparedStatement.setNull(3, Types.NULL);
            } else {
                preparedStatement.setLong(3, user.getCompany().getId());
            }
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка сохранения в базу данных");
        }
    }

    @Override
    public boolean existById(Long id) {
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_USER_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isExists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return isExists;
    }

}
