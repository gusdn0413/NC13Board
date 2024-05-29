package controller;

import connector.ConnectionMaker;
import model.BoardDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardController {

    private String query;
    private final Connection connection;

    public BoardController(ConnectionMaker connectionMaker) {
        this.connection = connectionMaker.makeConnection();
    }

    public void register(BoardDTO boardDTO) {
        query = "INSERT INTO board(title, content, writer_id) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, boardDTO.getTitle());
            preparedStatement.setString(2, boardDTO.getContent());
            preparedStatement.setInt(3, boardDTO.getWriterId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BoardDTO> selectAll() {
        List<BoardDTO> list = new ArrayList<>();
        query = "SELECT * FROM board INNER JOIN user ON board.writer_id = user.id";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                BoardDTO boardDTO = getBoardDTOInfo(resultSet);

                list.add(boardDTO);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BoardDTO selectOne(int id) {
        query = "SELECT * FROM board INNER JOIN user ON board.writer_id = user.id WHERE board.id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return getBoardDTOInfo(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(BoardDTO boardDTO) {
        query = "UPDATE board SET title = ?, content = ?, modify_date = NOW() WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, boardDTO.getTitle());
            preparedStatement.setString(2, boardDTO.getContent());
            preparedStatement.setInt(3, boardDTO.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        query = "DELETE FROM board WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static BoardDTO getBoardDTOInfo(ResultSet resultSet) throws SQLException {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(resultSet.getInt("id"));
        boardDTO.setTitle(resultSet.getString("title"));
        boardDTO.setContent(resultSet.getString("content"));
        boardDTO.setEntryDate(resultSet.getTimestamp("entry_date"));
        boardDTO.setModifyDate(resultSet.getTimestamp("modify_date"));
        boardDTO.setWriterId(resultSet.getInt("writer_id"));
        boardDTO.setNickname(resultSet.getString("nickname"));
        return boardDTO;
    }
}
