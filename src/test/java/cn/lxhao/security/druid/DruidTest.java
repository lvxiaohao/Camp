package cn.lxhao.security.druid;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.Test;

/**
 * Created on 2015-04-30.
 *
 * @author 吕浩
 * @since 1.0.0
 */
public class DruidTest {
    /**
     * 将文本用druid加密.
     * @throws Exception
     */
    @Test
    public void generatePassword() throws Exception {
        String password = "Your password";
        System.out.println(ConfigTools.encrypt(password));
    }
}
