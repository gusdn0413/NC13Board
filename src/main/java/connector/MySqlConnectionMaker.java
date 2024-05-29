package connector;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySqlConnectionMaker implements ConnectionMaker {

    public final String URL = "jdbc:mysql://localhost:3306/board";
    public final String USERNAME = "root";
    public final String PASSWORD = "Qkr1593574!s";
    public final String DRIVER = "com.mysql.cj.jdbc.Driver";

    @Override
    public Connection makeConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
