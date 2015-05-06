package cn.lxhao.database.jdbc;

import cn.lxhao.database.jdbc.bean.Emp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class EmpManager {
    /**
     * 保存员工信息
     *
     * @param e 要保存的员工信息
     */
    public void saveEmp(Emp e) {
        String sql = "insert into s_emp(id,last_name,salary) values(?,?,?)";
        Connection conn = null;
        PreparedStatement state = null;
        try {
            conn = ConnectionFactory.getMysqlConnection();
            state = conn.prepareStatement(sql);
            state.setInt(1, e.getId());
            state.setString(2, e.getName());
            state.setDouble(3, e.getSalary());
            state.execute();
        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            ConnectionFactory.close(null, state, conn);
        }
    }

    /**
     * 通过ID得到员工信息
     *
     * @param id 员工的ID
     * @return 查询到的员工信息
     */
    public Emp getEmpById(int id) {
        String sql = "select id,last_name,salary from s_emp where id = ?";
        Emp p = null;
        Connection conn = null;
        PreparedStatement state = null;
        ResultSet result = null;
        try {
            conn = ConnectionFactory.getMysqlConnection();
            state = conn.prepareStatement(sql);
            state.setInt(1, id);
            result = state.executeQuery();

            // TODO 转换员工信息
            p = new Emp();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(result, state, conn);
        }
        return p;
    }

    /**
     * 通过姓名获取员工信息
     *
     * @param name
     * @return
     */
    public Emp getEmpByName(String name) {
        return null;
    }

    /**
     * 删除员工信息
     *
     * @param e
     */
    public void deleteEmp(Emp e) {

    }

    /**
     * 更新员工信息
     *
     * @param e
     */
    public void updateEmp(Emp e) {

    }
}
