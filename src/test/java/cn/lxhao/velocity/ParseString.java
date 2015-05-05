package cn.lxhao.velocity;

import cn.lxhao.velocity.directive.Name;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created on 2015-05-05.
 *
 * @author 吕浩
 * @since 1.0.0
 */
public class ParseString {
    String text;
    Map<String, Object> map;

    @Before
    public void init() {
        text =
                "#if($history.size() > 0)\n" +
                "<div class=\"am-list\">\n" +
                "    <div class=\"am-list-header\">\n" +
                "        #name(\"1\",\"历史记录\")\n" +
                "    </div>\n" +
                "    <div class=\"am-list-body\">\n" +
                "        #foreach($item in $history)\n" +
                "            <div class=\"am-list-item\" \">\n" +
                "                <div class=\"am-list-content \">\n" +
                "                    <div class=\"am-list-title f_left\">$item\n" +
                "                    </div>\n" +
                "                    <div class=\"am-list-extra   \">\n" +
                "                        <span class=\"am-ft-black am-ft-lightgray\">#name(\"张三\")</span>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        #end\n" +
                "    </div>\n" +
                "</div>\n" +
                "#end";

        map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        map.put("history", list);
    }

    @Test
    public void testParse() {
        // 初始化引擎
        VelocityEngine velocityEngine = VelocityFactory.getEngine();

        // 引入自定义指令
        velocityEngine.loadDirective(Name.class.getName());

        // 初始化上下文
        VelocityContext velocityContext = new VelocityContext(map);

        // 渲染文本
        StringWriter sw = new StringWriter();
        velocityEngine.evaluate(velocityContext, sw, "/ve.txt", text);
        System.out.println(sw);
    }
}
