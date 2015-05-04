package cn.lxhao.database.hibernate.pojo;

import java.io.Serializable;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class Emp implements Serializable {
    /**
     *
     */
    private long id;

    /**
     * 姓
     */
    private String last_name;

    /**
     * 薪水
     */
    private double salary;

    public Emp() {

    }

    public Emp(String last_name, double salary) {
        this.setLast_name(last_name);
        this.setSalary(salary);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
