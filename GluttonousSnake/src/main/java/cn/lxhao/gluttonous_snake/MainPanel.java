package cn.lxhao.gluttonous_snake;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Random;

public class MainPanel extends JPanel implements KeyListener, Runnable {
    static int length;                //身体的长度
    int game_level = 1;                //游戏的等级
    int game_exp = 0;                //升级所需要的经验
    int direction;                    //前进的方向
    int head_x, head_y;                //头部的坐标
    int step_x, step_y;                //头部每一步在x,y上的位移
    int food_x, food_y;                //食物的坐标
    boolean have_food;                //当前场景是否有食物
    boolean change_direction;        //是否可改变方向。当上一次更改方向未执行时，此值为false，将不响应按键操作。
    int state;                        //游戏状态	0为正在进行	-1为游戏结束 1为游戏暂停
    int sum_time;                    //游戏进行时长,单位为ms.
    File body[] = new File[7];        //存放身体图片
    MyQueue list = new MyQueue();    //存放身体坐标的队列

    boolean start = true;

    public MainPanel() {
        initialize();                    //初始化
        this.addKeyListener(this);        //添加键盘侦听
        this.setFocusable(true);
        new Thread(this).start();        //启动线程
        for (int i = 0; i < 7; i++)            //载入身体的图片
        {
            try {
                body[i] = new File(this.getClass().getResource("/image/body_" + i + ".png").toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ------------初始化--------------------
     */
    public void initialize() {
        state = 0;        //--------标记游戏为开始状态-------
        head_x = 50;    //-----------头部坐标-----------
        head_y = 50;    //
        length = 1;        //---------设置身体长度-----------
        list.add(new Point(50, 50));    //
        length = 5;        //
        direction = 3;    //--------默认方向,向右-----------
        step_x = 50;    //
        step_y = 0;        //
        sum_time = 0;    //-----------时间清零------------
        have_food = false;
        food_x = 300;
        food_y = 50;
    }

    /**
     * 启动的线程
     */
    public void run() {
        while (true)        //不间断执行
        {
            int time_s = 500 - game_level * 30;
            sum_time += time_s;
            try {
                Thread.sleep(time_s);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (state == 0) {
                head_x += step_x;
                head_y += step_y;
                this.repaint();
                change_direction = true;
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        //判断是否越界，如果越界，则游戏结束
        if (!judge(head_x, head_y, 1)) {
            g.setColor(new Color(255, 0, 0));
            this.setBackground(new Color(0, 0, 0));
            try {
                Image image = ImageIO.read(new File(this.getClass().getResource("/image/game_over.png").toURI()));
                g.drawImage(image, this.getWidth() / 2 - image.getWidth(null) / 2, this.getHeight() / 2 - image.getHeight(null) / 2, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.repaint();
            state = -1;            //标记游戏结束
            change_direction = true;
            step_x = 0;
            step_y = 0;
        } else {
            this.setBackground(new Color(255, 255, 255));


            if (head_x == food_x && head_y == food_y)    //当吃到食物时,
            {
                length++;        //蛇身长度加1;
                game_exp++;
                if (game_exp == game_level + 4) {
                    game_exp = 0;
                    game_level++;
                }
                have_food = false;
                new Player("eat_food");
            }

            ////-----------画蛇身------------
            for (int i = 0; i < list.size(); i++) {
                //--------画一节蛇身-----------
                paintBody(g, list.get(i).x, list.get(i).y, list.size() - i);
            }
            ////------------画头-----------
            paintHead(g, head_x, head_y);
            //如果没有越界，则将蛇头坐标加入队列
            list.add(new Point(head_x, head_y));
        }
    }

    public void paintHead(Graphics g, int head_x, int head_y) {
        try {
            Image image = ImageIO.read(new File(this.getClass().getResource("/image/head_" + direction + ".png").toURI()));
            g.drawImage(image, head_x - 20, head_y - 20, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paintBody(Graphics g, int body_x, int body_y, int no) {
        try {
            Image image = ImageIO.read(body[no % 7]);
            g.drawImage(image, body_x, body_y, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean judge(int x, int y, int k)        //k代表调取源
    {
        int width = this.getWidth();
        int height = this.getHeight();
        int temp;
        if (k == 1) {
            //如果撞到墙壁
            if (x < 50 || y < 50 || x > width - 50 || y > height - 50)
                return false;
            temp = 3;
        } else
            temp = 0;
        //如果撞到蛇身
        for (int i = 0; i < list.size() - temp; i++) {
            if (x == list.get(i).x && y == list.get(i).y) {
                return false;
            }
        }
        return true;
    }

    ////////--------------绘制边框及食物-------------////////
    public void paintBorder(Graphics g) {
        //--------------绘制边框四条线--------------
        g.setColor(new Color(0, 0, 0));
        g.drawLine(50, 50, this.getWidth() / 50 * 50, 50);
        g.drawLine(50, 50, 50, this.getHeight() / 50 * 50);
        g.drawLine(this.getWidth() / 50 * 50, 50, this.getWidth() / 50 * 50, this.getHeight() / 50 * 50);
        g.drawLine(50, this.getHeight() / 50 * 50, this.getWidth() / 50 * 50, this.getHeight() / 50 * 50);
        //-----------------如果没有食物,产生新的食物,----------------
        if (!have_food) {
            Random random = new Random();
            int judge = random.nextInt(4) + 3;
            if (judge == 4)    //判断随机数，实现随机间隔时间产生食物的效果。
                paintFood(g);
        }
        if (state == 0) {
            if (have_food)
                //----------------------绘出食物.----------------------
                try {
                    Image image = ImageIO.read(new File(this.getClass().getResource("/image/food.png").toURI()));
                    g.drawImage(image, food_x + 5, food_y - 13, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //----------------------绘出游戏信息---------------------
            g.setColor(new Color(100, 100, 200));
            g.drawString("当前长度" + list.size(), 200, 30);
            g.drawString("已用时间: " + sum_time / 60000 + "分   " + (sum_time / 1000) % 60 + "秒", 300, 30);
            g.drawString("当前速度：" + (50000 / (500 - game_level * 30)), 500, 30);
            g.drawString("等级:" + game_level, 620, 30);
            g.drawString("经验值:" + game_exp + "/" + (game_level + 4), 660, 30);
        }
    }

    public void paintFood(Graphics g) {
        Random random = new Random();
        do            //产生随机坐标，如果坐标与蛇身冲突，重新产生。
        {
            food_x = random.nextInt(this.getWidth() / 50 - 1) * 50 + 50;
            food_y = random.nextInt(this.getHeight() / 50 - 1) * 50 + 50;
        }
        while (!judge(food_x, food_y, 2));    //当发生冲突。
        have_food = true;                //产生新食物后，将have_food标记为true;
    }

    public void keyPressed(KeyEvent arg0) {
        if (change_direction) {
            int code = arg0.getKeyCode();//----------获取键码------
            switch (code)                //-----------筛选按键-------
            {
                case 37:
                    //------如果方向相斥，则不做操作------
                    if (direction == 1 || direction == 3)
                        break;
                    step_x = -50;
                    step_y = 0;
                    direction = 1;
                    change_direction = false;
                    break;
                case 38:
                    if (direction == 2 || direction == 4)
                        break;
                    step_x = 0;
                    step_y = -50;
                    direction = 2;
                    change_direction = false;
                    break;
                case 39:
                    if (direction == 1 || direction == 3)
                        break;
                    step_x = 50;
                    step_y = 0;
                    direction = 3;
                    change_direction = false;
                    break;
                case 40:
                    if (direction == 2 || direction == 4)
                        break;
                    step_x = 0;
                    step_y = 50;
                    direction = 4;
                    change_direction = false;
                    break;
                case 32:            //空格键复位
                    initialize();
                    break;
            }
        }
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }
}