package cn.lxhao.velocity.directive;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import java.io.IOException;
import java.io.Writer;

/**
 * Created on 2015-05-05.
 *
 * @author 吕浩
 * @since 1.0.0
 */
public class Name extends Directive {

    @Override
    public String getName() {
        return "name";
    }

    @Override
    public int getType() {
        return LINE;
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        // 筛选参数数量(假重载)
        int number = node.jjtGetNumChildren();
        switch (number) {
            case 0:
                break;
            case 1: {
                // 只有一个参数时,取该参数,显示
                SimpleNode sn = (SimpleNode) node.jjtGetChild(0);
                writer.write((String) sn.value(context));
                return true;
            }
            case 2: {
                // 有两个参数时,取后面一个参数显示
                SimpleNode sn = (SimpleNode) node.jjtGetChild(1);
                writer.write((String) sn.value(context));
                return true;
            }
        }
        return false;
    }
}
