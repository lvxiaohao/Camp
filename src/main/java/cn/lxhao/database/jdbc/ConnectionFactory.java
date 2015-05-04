package cn.lxhao.database.jdbc;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class ConnectionFactory {
    public static Connection getConnection() {
        return getMysqlConnection();
    }

    public static Connection getOracleConnection() {
        Connection conn = null;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("database/jdbc/oracle.jdbc.properties"));
            Class.forName(prop.getProperty("driver"));
            conn = DriverManager.getConnection(prop.getProperty("url"), prop);
        } catch (Exception e) {
            System.out.println("创建数据库连接失败。");
            e.printStackTrace();
        }
        return conn;
    }
    public static Connection getMysqlConnection() {
        Connection conn = null;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("database/jdbc/mysql.jdbc.properties"));
            Class.forName(prop.getProperty("driver"));
            conn = DriverManager.getConnection(prop.getProperty("url"), prop);
        } catch (Exception e) {
            System.out.println("创建数据库连接失败。");
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(ResultSet rs, Statement state, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (state != null) {
            try {
                state.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
