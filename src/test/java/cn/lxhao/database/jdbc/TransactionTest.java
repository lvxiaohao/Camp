package cn.lxhao.database.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class TransactionTest {
    @Test
    public void test() {
        //事务的边界
        Connection conn = null;        //同时只能处理一个事务
        Statement state = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);                        //事务开启
            String sql1 = "update s_emp set dept_id = 32 where id = 12";
            String sql2 = "update s_emp set dept_id = 34 where id = 14";
            state = conn.createStatement();
            state.execute(sql1);
            int a = 1 / 0;    // 产生异常
            state.execute(sql2);
        } catch (Exception e) {
            try {
                //SQLException
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();

        } finally {
            try {
                if (conn != null) {
                    conn.commit();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConnectionFactory.close(null, state, conn);
        }
    }
}
