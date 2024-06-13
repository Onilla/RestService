package com.homework.repository.impl;

import com.homework.connection.ConnectionManager;
import com.homework.connection.ConnectionManagerImpl;
import com.homework.entity.Company;
import com.homework.entity.Position;
import com.homework.entity.User;
import com.homework.exception.DBException;
import com.homework.repository.CompanyRepository;
import com.homework.repository.PositionRepository;
import com.homework.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {


//    private final String FIND_USERS_BY_POSITION_ID = """
//            SELECT users_positions_id, user_id, position_id FROM users_positions
//            WHERE position_id = ?;
//            """;

//    private final String EXIST_USER_BY_ID_FROM_UP = """
//            SELECT exists (
//                SELECT 1 FROM users_positions
//                WHERE user_id = ?);
//            """;

    private ConnectionManager connectionManager = new ConnectionManagerImpl();
    private CompanyRepository companyRepository = new CompanyRepositoryImpl();


    private User createUser(ResultSet resultSet) throws SQLException {
        String FIND_ALL_POSITIONS_BY_USER_ID = """
            SELECT p.position_id, position_name FROM positions AS p
            INNER JOIN users_positions AS up ON p.position_id = up.position_id
            INNER JOIN users AS u ON u.user_id = up.user_id
            WHERE u.user_id = ?;
            """;
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_POSITIONS_BY_USER_ID)){

            Long userId = resultSet.getLong("user_id");
            preparedStatement.setLong(1, userId);
            ResultSet resultPositions = preparedStatement.executeQuery();
            List<Position> positions = new ArrayList<>();
            while (resultPositions.next()) {
                Position position = new Position(resultPositions.getLong("position_id"),
                        resultPositions.getString("position_name"));
                positions.add(position);
            }
            Company company = companyRepository.findById(resultSet.getLong("company_id")).orElse(null);
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

//    @Override
//    public List<User> findUsersByPositionId(Long positionId) {
//        List<User> users = new ArrayList<>();
//        try (Connection connection = connectionManager.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(FIND_USERS_BY_POSITION_ID)) {
//
//            preparedStatement.setLong(1, positionId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                long userId = resultSet.getLong("user_id");
//                Optional<User> optionalUser = findById(userId);
//                if (optionalUser.isPresent()) {
//                    users.add(optionalUser.get());
//                }
//            }
//        } catch (SQLException e) {
//            e.getMessage();
//        }
//        return users;
//    }

    @Override
    public List<User> findAll() {
        String FIND_ALL_USERS = """
            SELECT user_id, user_firstname, user_lastname, company_id FROM users ;
            """;
        List<User> usersList = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection()) {
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS);

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
        String SAVE_USERS_AND_POSITIONS = "INSERT INTO users_positions (user_id, position_id) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS);

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
                user.setId(resultSet.getLong(1));
            }
            if (!user.getPositions().isEmpty()) {
                preparedStatement = connection.prepareStatement(SAVE_USERS_AND_POSITIONS);
                for (Position position : user.getPositions()) {
                    preparedStatement.setLong(1, user.getId());
                    preparedStatement.setLong(2, position.getId());
                    preparedStatement.executeUpdate();
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
        String UPDATE_USERS_AND_POSITIONS ="UPDATE users_positions SET position_id = ? WHERE user_id=?;";
        try (Connection connection = connectionManager.getConnection()){
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, user.getFirstname());
            preparedStatement.setString(2, user.getLastname());
            if (user.getCompany() == null) {
                preparedStatement.setNull(3, Types.NULL);
            } else {
                preparedStatement.setLong(3, user.getCompany().getId());
            }
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
            if (!user.getPositions().isEmpty()) {
                preparedStatement = connection.prepareStatement(UPDATE_USERS_AND_POSITIONS);
                for (Position position : user.getPositions()) {
                    preparedStatement.setLong(1, position.getId());
                    preparedStatement.setLong(2, user.getId());
                    preparedStatement.executeUpdate();
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
        try (Connection connection = connectionManager.getConnection()) {
            PreparedStatement deleteUsersPositions = connection.prepareStatement(DELETE_USER_POSITIONS_BY_ID);
            connection.setAutoCommit(false);
            deleteUsersPositions.setLong(1, id);
            deleteUsersPositions.executeUpdate();
            PreparedStatement deleteUser = connection.prepareStatement(DELETE_USER_BY_ID);
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
        try (Connection connection = connectionManager.getConnection()) {
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_USER_BY_ID);

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
