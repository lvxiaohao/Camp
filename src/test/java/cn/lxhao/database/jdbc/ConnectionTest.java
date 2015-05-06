package cn.lxhao.database.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class ConnectionTest {
    @Test
    public void test() {
        Connection conn = null;
        Statement state = null;
        ResultSet result = null;
        try {
            conn = ConnectionFactory.getOracleConnection();
            state = conn.createStatement();
            result = state.executeQuery("select * from jdbc_test");
            while (result.next()) {
                System.out.print(result.getInt(1) + "\t");
                System.out.println(result.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(result, state, conn);
        }
    }
}
