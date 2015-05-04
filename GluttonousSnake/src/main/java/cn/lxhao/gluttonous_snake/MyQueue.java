package cn.lxhao.gluttonous_snake;

import java.awt.*;
import java.util.LinkedList;

/**
 * 定义蛇身坐标队列类，此类用于存放每一节蛇身的坐标。
 *
 * @author Administrator
 */
public class MyQueue {
    LinkedList<Point> list = new LinkedList<Point>();

    public void add(Point p)        //新增一节蛇身，如果队列内坐标数大于实际长度，删除队列第一组数据（即删除最后一节蛇身）
    {
        list.addLast(p);
        while (list.size() > MainPanel.length)    //当头在移动时，会不断把新的头部坐标加入队列，此时，size会大于length，
        {                            //删除身体的第一节，即可保持身体的长度不变。实现移动效果。
            //判断是否为空
            if (!list.isEmpty())
                list.removeFirst();
        }
    }

    public Point get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }
}