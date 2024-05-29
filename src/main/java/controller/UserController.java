package controller;

import connector.ConnectionMaker;
import model.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {
    private String query;
    private final Connection connection;

    public UserController(ConnectionMaker connectionMaker) {
        connection = connectionMaker.makeConnection();
    }

    public UserDTO auth(String username, String password) {
        query = "SELECT * FROM user WHERE username = ? AND password =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(resultSet.getInt("id"));
                userDTO.setUsername(resultSet.getString("username"));
                userDTO.setPassword(resultSet.getString("password"));
                userDTO.setNickname(resultSet.getString("nickname"));

                return userDTO;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(UserDTO userDTO) {
        query = "INSERT INTO user(username, password, nickname) VALUES(?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userDTO.getUsername());
            preparedStatement.setString(2, userDTO.getPassword());
            preparedStatement.setString(3, userDTO.getNickname());
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean update(UserDTO userDTO) {
        query = "UPDATE user SET password = ?, nickname = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userDTO.getPassword());
            preparedStatement.setString(2, userDTO.getNickname());
            preparedStatement.setInt(3, userDTO.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void delete(int id) {
        query = "DELETE FROM user WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() throws SQLException {
        query = "SET foreign_key_checks = 0";


        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();

        query = "TRUNCATE TABLE user";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();

        query = "TRUNCATE TABLE board";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();

        query = "TRUNCATE TABLE reply";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();

        query = "SET foreign_key_checks = 1";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }
}
