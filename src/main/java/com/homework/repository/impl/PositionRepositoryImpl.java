package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.exception.DBException;
import com.homework.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionRepositoryImpl implements Repository<Position, Long> {

    private final ConnectionManager connectionManager = new ConnectionManagerImpl();

    private final Repository<Company, Long> companyRepository = new CompanyRepositoryImpl();

    @Override
    public Optional<Position> findById(Long id) {
        String FIND_POSITION_BY_ID = """
            SELECT position_id, position_name FROM positions
            WHERE position_id = ?;
            """;
        Position position = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_POSITION_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                position = createPosition(resultSet);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return Optional.ofNullable(position);
    }

    private Position createPosition(ResultSet resultSet) throws SQLException {
        String FIND_ALL_USERS_BY_POSITIONS_ID = """
            SELECT u.user_id, user_firstname, user_lastname, u.company_id FROM users AS u
            INNER JOIN users_positions AS up ON u.user_id = up.position_id
            INNER JOIN positions AS p ON p.position_id = up.position_id
            WHERE p.position_id = ?;
            """;

        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS_BY_POSITIONS_ID)) {

            Long positionId = resultSet.getLong("position_id");
            preparedStatement.setLong(1, positionId);
            ResultSet resultUsers = preparedStatement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultUsers.next()) {
                User user = new User(resultUsers.getLong("user_id"),
                        resultUsers.getString("user_firstname"),
                        resultUsers.getString("user_lastname"),
                        companyRepository.findById(resultUsers.getLong("company_id")).orElse(null));
                users.add(user);
            }
            return new Position(positionId,
                    resultSet.getString("position_name"), users);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        String DELETE_POSITION_BY_USER_ID = """
            DELETE FROM users_positions
            WHERE position_id = ? ;
            """;
       String DELETE_POSITION_BY_ID = """
            DELETE FROM positions
            WHERE position_id = ? ;
            """;
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement deleteUsersPositions = connection.prepareStatement(DELETE_POSITION_BY_USER_ID);
            connection.setAutoCommit(false);
            deleteUsersPositions.setLong(1, id);
            deleteUsersPositions.executeUpdate();
            PreparedStatement deletePosition= connection.prepareStatement(DELETE_POSITION_BY_ID);
            deletePosition.setLong(1, id);
            deleteResult = deletePosition.executeUpdate() > 0;
            connection.commit();
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return deleteResult;
    }

    @Override
    public List<Position> findAll() {
        List<Position> positionList = new ArrayList<>();
        String FIND_ALL_POSITIONS = """
            SELECT position_id, position_name FROM positions ;
            """;
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_POSITIONS)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                positionList.add(createPosition(resultSet));
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return positionList;
    }

    @Override
    public Position save(Position position) {
        String SAVE_POSITION = """
            INSERT INTO positions (position_name)
            VALUES (?) ;
            """;
        String SAVE_USERS_AND_POSITIONS = "INSERT INTO users_positions (user_id, position_id) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_POSITION, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, position.getName());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                position.setId(resultSet.getLong("position_id"));
            }
            if (!position.getUsers().isEmpty()) {
                preparedStatement = connection.prepareStatement(SAVE_USERS_AND_POSITIONS);
                for (User user : position.getUsers()) {
                    preparedStatement.setLong(1, user.getId());
                    preparedStatement.setLong(2, position.getId());
                    preparedStatement.executeUpdate();
                }
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DBException(e);
        }

        return position;

    }

    @Override
    public void update(Position position) {
        String UPDATE_POSITION = """
            UPDATE positions
            SET position_name = ?
            WHERE position_id = ?  ;
            """;
        String UPDATE_USERS_AND_POSITIONS = "UPDATE users_positions SET user_id = ? WHERE position_id=?;";
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POSITION);
            connection.setAutoCommit(false);
            preparedStatement.setString(1, position.getName());

            preparedStatement.setLong(2, position.getId());
            preparedStatement.executeUpdate();
            if (!position.getUsers().isEmpty()) {
                preparedStatement = connection.prepareStatement(UPDATE_USERS_AND_POSITIONS);
                for (User user : position.getUsers()) {
                    preparedStatement.setLong(1, user.getId());
                    preparedStatement.setLong(2, position.getId());
                    preparedStatement.executeUpdate();

                }
            }
            connection.commit();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public boolean existById(Long id) {
        boolean isExists = false;
        String EXIST_POSITION_BY_ID = """
                SELECT exists (
                SELECT 1 FROM positions
                WHERE position_id = ?);
            """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_POSITION_BY_ID)) {

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
