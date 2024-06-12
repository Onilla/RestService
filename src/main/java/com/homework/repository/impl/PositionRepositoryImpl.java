package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.repository.PositionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PositionRepositoryImpl implements PositionRepository {
    private final ConnectionManager connectionManager = new ConnectionManagerImpl();
    private final String SAVE_POSITION = """
            INSERT INTO positions (position_name)
            VALUES (?) ;
            """;
    private final String FIND_POSITION_BY_ID = """
            SELECT position_id, position_name FROM positions
            WHERE position_id = ?;
            """;
    private final String FIND_POSITION_BY_USER_ID = """
            SELECT position_id FROM users_positions
            WHERE user_id = ?;
            """;


    @Override
    public List<Long> findPositionIdByUserId(Long id) {
        List<Long> positions = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_POSITION_BY_USER_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long positionId = resultSet.getLong("position_id");
                positions.add(positionId);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return positions;
    }

    @Override
    public Optional<Position> findById(Long id) {
        Position position = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_POSITION_BY_ID)) {

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                position = createPosition(resultSet);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return Optional.ofNullable(position);
    }

    private Position createPosition(ResultSet resultSet) throws SQLException {

        return new Position(resultSet.getLong("position_id"),
                resultSet.getString("position_name"), null);

    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public List<Position> findAll() {
        return null;
    }

    @Override
    public Position save(Position position) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_POSITION, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, position.getName());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                position = new Position(
                        resultSet.getLong("position_id"),
                        position.getName(),
                        null
                );
                position.getUsers();
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return position;
    }

    @Override
    public void update(Position position) {

    }

    @Override
    public boolean existById(Long id) {
        return false;
    }
}
