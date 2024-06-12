package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.entity.Position;
import com.homework.repository.PositionRepository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PositionRepositoryImpl implements PositionRepository {
    private final ConnectionManager connectionManager = new ConnectionManagerImpl();
    private final String SAVE_POSITION = """
            INSERT INTO positions (position_name)
            VALUES (?) ;
            """;

    @Override
    public Optional<Position> findById(Long id) {
        return Optional.empty();
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
