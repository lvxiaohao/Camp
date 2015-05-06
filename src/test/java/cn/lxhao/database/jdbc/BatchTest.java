package cn.lxhao.database.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class BatchTest {
    @Test
    public void testStatement() {
        String sql1 = "insert into jdbc_test(id,name) values (11,'haha')";
        String sql2 = "insert into jdbc_test(id,name) values (12,'haha')";
        String sql3 = "insert into jdbc_test(id,name) values (13,'haha')";
        String sql4 = "insert into jdbc_test(id,name) values (14,'haha')";
        String sql5 = "insert into jdbc_test(id,name) values (15,'haha')";
        String sql6 = "delete from jdbc_test where name = 'Smith' ";

        Connection conn = null;
        Statement state = null;
        try {
            conn = ConnectionFactory.getConnection();
            state = conn.createStatement();
            state.addBatch(sql1);
            state.addBatch(sql2);
            state.addBatch(sql3);
            state.addBatch(sql4);
            state.addBatch(sql5);
            state.addBatch(sql6);
            state.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(null, state, conn);
        }
    }

    @Test
    public void testPreparedStatement() {
        String sql = "insert into jdbc_test(id,name) values(?,?)";

        Connection conn = null;
        PreparedStatement state = null;
        try {
            conn = ConnectionFactory.getConnection();
            state = conn.prepareStatement(sql);
            long startTime = System.currentTimeMillis();
            for (int i = 1; i < 1000000; i++) {
                state.setInt(1, i);
                state.setString(2, "test_" + i);
                state.addBatch();    //将一组参数添加到命令列表中，在所有参数已经赋值后使用此语句
            }
            state.executeBatch();    //没有参数
            long midTime = System.currentTimeMillis();
            int count = 1;
            for (int i = 1; i < 1000000; i++) {
                state.setInt(1, i);
                state.setString(2, "test_" + i);
                state.addBatch();    //将一组参数添加到命令列表中，在所有参数已经赋值后使用此语句
                count++;
                if (count % 100000 == 0) {
                    state.executeBatch();
                }
            }
            state.executeBatch();    //没有参数
            long endTime = System.currentTimeMillis();
            System.out.println("第一次运行的时间" + (midTime - startTime));
            System.out.println("第二次运行的时间" + (endTime - midTime));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(null, state, conn);
        }
    }
}
