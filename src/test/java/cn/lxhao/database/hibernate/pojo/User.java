package cn.lxhao.database.hibernate.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class User implements Serializable {

    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 生日
     */
    private Date birthday;

    public User() {

    }

    public User(String id, String name, Date birthDate) {
        this.setId(id);
        this.setName(name);
        this.setBirthday(birthDate);
    }

    public User(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    @Id
    @GeneratedValue(generator = "xx")    //xx是生成器的名字，可任意,但要与下面的Name相同
    //--------uuid---------------
    @GenericGenerator(name = "xx", strategy = "uuid")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name")    //属性名跟列名不同时要写，相同时不用写
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthDate) {
        this.birthday = birthDate;
    }
}
