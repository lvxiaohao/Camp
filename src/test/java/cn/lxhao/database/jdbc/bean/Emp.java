package cn.lxhao.database.jdbc.bean;

import java.io.Serializable;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class Emp implements Serializable {

    private int id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 薪水
     */
    private double salary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Emp [id=" + id + ", name=" + name + ", salary=" + salary + "]";
    }
}
