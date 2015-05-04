package cn.lxhao.security.rsa;

import org.junit.Test;

import java.util.Map;

/**
 * 测试
 * Created on 2015-04-30.
 *
 * @author 吕浩
 * @since 1.0.0
 */
public class RsaTest {

    /**
     * 生成RSA密钥对.
     */
    @Test
    public void generatePrivateAndPublicKey() {

        Map<String, Object> keyMap;
        try {
            keyMap = Rsa.initKey();
            String publicKey = Rsa.getPublicKey(keyMap);
            System.out.println("------------ public key is ------------- ");
            System.out.println();
            System.out.println(publicKey);
            System.out.println();
            String privateKey = Rsa.getPrivateKey(keyMap);
            System.out.println("------------ private key is ------------- ");
            System.out.println();
            System.out.println(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}