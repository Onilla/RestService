package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.connection.ContainerConnectionManager;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.exception.DBException;
import com.homework.fabric.Fabric;
import com.homework.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements Repository<User, Long> {

    private ConnectionManager connectionManager;

    public UserRepositoryImpl() {
        this.connectionManager = Fabric.getConnectionManager();
    }

    public UserRepositoryImpl(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        String findAllPositionsByUserId = """
                SELECT p.position_id, position_name FROM positions AS p
                INNER JOIN users_positions AS up ON p.position_id = up.position_id
                INNER JOIN users AS u ON u.user_id = up.user_id
                WHERE u.user_id = ?;
                """;
        String findCompanyByCompanyId = """
                SELECT company_id, company_name FROM companies
                WHERE company_id = ?;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findPositions = connection.prepareStatement(findAllPositionsByUserId);
             PreparedStatement findCompany = connection.prepareStatement(findCompanyByCompanyId)) {
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
        String findUserById = """
                SELECT user_id, user_firstname, user_lastname, company_id FROM users
                WHERE user_id = ?;
                """;
        User user = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findUserById)) {
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
    public List<User> findAll() {
        String findAllUsers = """
                SELECT user_id, user_firstname, user_lastname, company_id FROM users ;
                """;
        List<User> usersList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllUsers)) {

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
        String save = """
                INSERT INTO users (user_firstname, user_lastname, company_id)
                VALUES (?, ? ,?) ;
                """;
        String saveUsersPositions =
                "INSERT INTO users_positions (user_id, position_id) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement saveUser = connection.prepareStatement(save, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement saveUsersAndPositions = connection.prepareStatement(saveUsersPositions)) {
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
        String update = """
                UPDATE users
                SET user_firstname = ?, user_lastname = ?, company_id = ?
                WHERE user_id = ?  ;
                """;
        String updateUsersPositions = """ 
                UPDATE users_positions
                 SET position_id = ?
                 WHERE user_id=?;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateUser = connection.prepareStatement(update);
             PreparedStatement updateUsersAndPositions = connection.prepareStatement(updateUsersPositions)) {
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
        String deleteUserById = """
                DELETE FROM users
                WHERE user_id = ? ;
                """;
        String deleteUserPositionsById = """
                DELETE FROM users_positions
                WHERE user_id = ? ;
                """;
        boolean deleteResult;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteUsersPositions = connection.prepareStatement(deleteUserPositionsById);
             PreparedStatement deleteUser = connection.prepareStatement(deleteUserById)) {
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
        String existUserById = """
                    SELECT exists (
                    SELECT 1 FROM users
                    WHERE user_id = ?);
                """;
        boolean isExists = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement existUser = connection.prepareStatement(existUserById)) {
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
