package cn.lxhao.gluttonous_snake;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        MainPanel panel = new MainPanel();
        this.add(panel);
        double width = Toolkit.getDefaultToolkit().getScreenSize().width; //得到当前屏幕分辨率的高
        double height = Toolkit.getDefaultToolkit().getScreenSize().height;//得到当前屏幕分辨率的宽
        this.setSize((int) width, (int) height);    //设置大小
        this.setLocation(0, 0);                    //设置窗体居中显示
        this.setResizable(false);                //禁用最大化按钮
        this.setTitle("贪吃蛇");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
