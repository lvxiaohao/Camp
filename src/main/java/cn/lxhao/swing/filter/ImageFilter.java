package cn.lxhao.swing.filter;

import cn.lxhao.swing.filter.support.ImageFileFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class ImageFilter {
    String path;

    public ImageFilter() {
        JFileChooser file = new JFileChooser("my documents");
        file.setAcceptAllFileFilterUsed(false);
        file.addChoosableFileFilter(new ImageFileFilter("jpg"));
        file.addChoosableFileFilter(new ImageFileFilter("png"));
        file.addChoosableFileFilter(new ImageFileFilter("bmp"));
        int result = file.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            this.path = file.getSelectedFile().getAbsolutePath();
        }
    }

    public String getPath() {
        return path;
    }

    /**
     * 获得ImageIcon对象,ImageIcon用于在JLabel(new JLabel()方法),JButton(setIcon方法)等控件上显示图片
     *
     * @param width
     * @param height
     * @return
     */
    public ImageIcon getImageIcon(int width, int height) {
        try {
            BufferedImage newImage = ImageIO.read(new File(path));        //源文件生成图像
            BufferedImage image = new BufferedImage(width, height, newImage.getType());     //对源文件图像进行缩放,缩放至自定义大小(不对源文件进行修改)
            ImageIcon imageIcon = new ImageIcon(image);          //定义图标图像为缩放后的图片
            Graphics g = image.getGraphics();
            g.drawImage(newImage, 0, 0, width, height, null);   //绘制出图片
            g.dispose();
            return imageIcon;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ImageIcon getImageIcon() {
        return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path));
    }
}

