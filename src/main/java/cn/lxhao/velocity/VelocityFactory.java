package cn.lxhao.velocity;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import java.util.Properties;

/**
 * Created on 2015-05-05.
 *
 * @author 吕浩
 * @since 1.0.0
 */
public class VelocityFactory {
    private static VelocityEngine engine;

    public static VelocityEngine getEngine() {
        if(engine == null) {
            Properties properties = new Properties();
            properties.setProperty("resource.loader", "class");
            properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
            properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
            properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");

            engine = new VelocityEngine(properties);

        }
        return engine;
    }
}
