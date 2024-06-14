package com.homework.repository.impl;
import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.exception.DBException;
import com.homework.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final ConnectionManager connectionManager = new ConnectionManagerImpl();

    private User createUser(ResultSet resultSet) throws SQLException {
        String FIND_ALL_POSITIONS_BY_USER_ID = """
                SELECT p.position_id, position_name FROM positions AS p
                INNER JOIN users_positions AS up ON p.position_id = up.position_id
                INNER JOIN users AS u ON u.user_id = up.user_id
                WHERE u.user_id = ?;
                """;
        String FIND_COMPANY_BY_COMPANY_ID = """
                SELECT company_id, company_name FROM companies
                WHERE company_id = ?;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findPositions = connection.prepareStatement(FIND_ALL_POSITIONS_BY_USER_ID);
             PreparedStatement findCompany = connection.prepareStatement(FIND_COMPANY_BY_COMPANY_ID)) {
            long companyId = resultSet.getLong("company_id");
            Company company = new Company();
            findCompany.setLong(1, companyId);
            ResultSet resultCompany = findCompany.executeQuery();
            if (resultCompany.next()) {
                String companyName = resultCompany.getString("company_name");
                company.setId(companyId);
                company.setName(companyName);
            }
            long userId = resultSet.getLong("user_id");
            findPositions.setLong(1, userId);
            List<Position> positions = new ArrayList<>();
            ResultSet resultPositions = findPositions.executeQuery();
            while (resultPositions.next()) {
                Position position = new Position(resultPositions.getLong("position_id"),
                        resultPositions.getString("position_name"));
                positions.add(position);
            }
            return new User(userId,
                    resultSet.getString("user_firstname"),
                    resultSet.getString("user_lastname"),
                    company, positions);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String FIND_USER_BY_ID = """
                SELECT user_id, user_firstname, user_lastname, company_id FROM users
                WHERE user_id = ?;
                """;
        User user = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = createUser(resultSet);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findByCompanyId(Long id) {
        String FIND_USER_BY_COMPANY_ID = """
                SELECT user_id, user_firstname, user_lastname, company_id FROM users
                WHERE company_id = ?;
                """;
        List<User> usersList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_COMPANY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                usersList.add(createUser(resultSet));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return usersList;
    }

    @Override
    public List<User> findAll() {
        String FIND_ALL_USERS = """
                SELECT user_id, user_firstname, user_lastname, company_id FROM users ;
                """;
        List<User> usersList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                usersList.add(createUser(resultSet));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return usersList;
    }

    @Override
    public User save(User user) {
        String SAVE_USER = """
                INSERT INTO users (user_firstname, user_lastname, company_id)
                VALUES (?, ? ,?) ;
                """;
        String SAVE_USERS_AND_POSITIONS =
                "INSERT INTO users_positions (user_id, position_id) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement saveUser = connection.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement saveUsersAndPositions = connection.prepareStatement(SAVE_USERS_AND_POSITIONS)) {
            connection.setAutoCommit(false);
            saveUser.setString(1, user.getFirstname());
            saveUser.setString(2, user.getLastname());
            if (user.getCompany() == null) {
                saveUser.setNull(3, Types.NULL);
            } else {
                saveUser.setLong(3, user.getCompany().getId());
            }
            saveUser.executeUpdate();

            ResultSet resultSet = saveUser.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
            }
            if (!user.getPositions().isEmpty()) {
                for (Position position : user.getPositions()) {
                    saveUsersAndPositions.setLong(1, user.getId());
                    saveUsersAndPositions.setLong(2, position.getId());
                    saveUsersAndPositions.executeUpdate();
                }
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return user;
    }

    @Override
    public void update(User user) {
        String UPDATE_USER = """
                UPDATE users
                SET user_firstname = ?, user_lastname = ?, company_id = ?
                WHERE user_id = ?  ;
                """;
        String UPDATE_USERS_AND_POSITIONS = """ 
                UPDATE users_positions
                 SET position_id = ?
                 WHERE user_id=?;
                """;
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement updateUser = connection.prepareStatement(UPDATE_USER);
            PreparedStatement updateUsersAndPositions = connection.prepareStatement(UPDATE_USERS_AND_POSITIONS))
        {
            connection.setAutoCommit(false);
            updateUser.setString(1, user.getFirstname());
            updateUser.setString(2, user.getLastname());
            if (user.getCompany() == null) {
                updateUser.setNull(3, Types.NULL);
            } else {
                updateUser.setLong(3, user.getCompany().getId());
            }
            updateUser.setLong(4, user.getId());
            updateUser.executeUpdate();
            if (!user.getPositions().isEmpty()) {
                for (Position position : user.getPositions()) {
                    updateUsersAndPositions.setLong(1, position.getId());
                    updateUsersAndPositions.setLong(2, user.getId());
                    updateUsersAndPositions.executeUpdate();
                }
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String DELETE_USER_BY_ID = """
                DELETE FROM users
                WHERE user_id = ? ;
                """;
        String DELETE_USER_POSITIONS_BY_ID = """
                DELETE FROM users_positions
                WHERE user_id = ? ;
                """;
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement deleteUsersPositions = connection.prepareStatement(DELETE_USER_POSITIONS_BY_ID);
            PreparedStatement deleteUser = connection.prepareStatement(DELETE_USER_BY_ID)) {
            connection.setAutoCommit(false);
            deleteUsersPositions.setLong(1, id);
            deleteUsersPositions.executeUpdate();
            deleteUser.setLong(1, id);
            deleteResult = deleteUser.executeUpdate() > 0;
            connection.commit();
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return deleteResult;
    }

    @Override
    public boolean existById(Long id) {
        String EXIST_USER_BY_ID = """
                    SELECT exists (
                    SELECT 1 FROM users
                    WHERE user_id = ?);
                """;
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement existUser = connection.prepareStatement(EXIST_USER_BY_ID)) {

            existUser.setLong(1, id);

            ResultSet resultSet = existUser.executeQuery();
            if (resultSet.next()) {
                isExists = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return isExists;
    }

}
