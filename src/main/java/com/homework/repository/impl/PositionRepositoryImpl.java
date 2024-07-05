package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.connection.ContainerConnectionManager;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.exception.DBException;
import com.homework.fabric.Fabric;
import com.homework.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionRepositoryImpl implements Repository<Position, Long> {

    private ConnectionManager connectionManager;

    public PositionRepositoryImpl(){
        this.connectionManager = Fabric.getConnectionManager();
    }

    public PositionRepositoryImpl(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;}


    @Override
    public Optional<Position> findById(Long id) {
        String findPositionById = """
                SELECT position_id, position_name FROM positions
                WHERE position_id = ?;
                """;
        Position position = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findPositionById)) {

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
        String findAllUsersByPositionsId = """
                SELECT u.user_id, user_firstname, user_lastname, u.company_id FROM users AS u
                INNER JOIN users_positions AS up ON u.user_id = up.user_id
                INNER JOIN positions AS p ON p.position_id = up.position_id
                WHERE p.position_id = ?;
                """;

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement findUsers = connection.prepareStatement(findAllUsersByPositionsId)) {
            long positionId = resultSet.getLong("position_id");
            findUsers.setLong(1, positionId);
            ResultSet resultUsers = findUsers.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultUsers.next()) {
                User user = new User(resultUsers.getLong("user_id"),
                        resultUsers.getString("user_firstname"),
                        resultUsers.getString("user_lastname"));
                users.add(user);
            }
            return new Position(positionId,
                    resultSet.getString("position_name"), users);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean deleteResult;
        String deletePositionByUserId = """
                DELETE FROM users_positions
                WHERE position_id = ? ;
                """;
        String deletePositionById = """
                DELETE FROM positions
                WHERE position_id = ? ;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deletePosition = connection.prepareStatement(deletePositionById);
             PreparedStatement deleteUsersPositions = connection.prepareStatement(deletePositionByUserId)) {
            connection.setAutoCommit(false);
            deleteUsersPositions.setLong(1, id);
            deleteUsersPositions.executeUpdate();
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
        String findAllPositions = """
                SELECT position_id, position_name FROM positions ;
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAllPositions)) {

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
        String save = """
                INSERT INTO positions (position_name)
                VALUES (?) ;
                """;
        String saveUsersAndPositions = "INSERT INTO users_positions (user_id, position_id) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement savePosition = connection.prepareStatement(save, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement saveUsersAndPosition = connection.prepareStatement(saveUsersAndPositions)) {
            connection.setAutoCommit(false);

            savePosition.setString(1, position.getName());

            savePosition.executeUpdate();

            ResultSet resultSet = savePosition.getGeneratedKeys();
            if (resultSet.next()) {
                position.setId(resultSet.getLong("position_id"));
            }
            if (!position.getUsers().isEmpty()) {

                for (User user : position.getUsers()) {
                    saveUsersAndPosition.setLong(1, user.getId());
                    saveUsersAndPosition.setLong(2, position.getId());
                    saveUsersAndPosition.executeUpdate();
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
        String update = """
                UPDATE positions
                SET position_name = ?
                WHERE position_id = ?  ;
                """;
        String updateUsersAndPositions = "UPDATE users_positions SET user_id = ? WHERE position_id=?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updatePosition = connection.prepareStatement(update);
             PreparedStatement updateUsersAndPosition = connection.prepareStatement(updateUsersAndPositions)) {
            connection.setAutoCommit(false);
            updatePosition.setString(1, position.getName());
            updatePosition.setLong(2, position.getId());
            updatePosition.executeUpdate();
            if (!position.getUsers().isEmpty()) {
                for (User user : position.getUsers()) {
                    updateUsersAndPosition.setLong(1, user.getId());
                    updateUsersAndPosition.setLong(2, position.getId());
                    updateUsersAndPosition.executeUpdate();
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
        String existPositionById = """
                    SELECT exists (
                    SELECT 1 FROM positions
                    WHERE position_id = ?);
                """;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(existPositionById)) {

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
