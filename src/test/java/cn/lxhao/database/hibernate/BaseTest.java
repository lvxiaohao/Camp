package cn.lxhao.database.hibernate;

import cn.lxhao.database.hibernate.pojo.Emp;
import cn.lxhao.database.hibernate.pojo.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Test;

import java.util.Date;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class BaseTest {
    @Test
    public void saveUser() {
        Configuration config = new Configuration();
        config.configure();        //解析配置文件 无参即	是在src/hibernate.cfg.xml（逐行解析）
        SessionFactory fac = config.buildSessionFactory();
        Session session = fac.openSession();
        session.beginTransaction();

        User u = new User("saveUser", new Date());
        session.save(u);
        session.getTransaction().commit();

        session.close();
//		fac.close();
    }

    @Test
    public void saveEmp() {
        Emp emp = new Emp("张", 123);

        // 完整写法
        // Configuration config = new Configuration();
        // config.configure();
        // SessionFactory factory = config.buildSessionFactory();
        // Session session = factory.openSession();

        // 简化写法
        Session session = new Configuration().configure().buildSessionFactory().openSession();
        session.beginTransaction();
        session.save(emp);
        session.getTransaction().commit();
        session.close();
    }
}
