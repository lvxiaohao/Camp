package cn.lxhao.swing.filter.support;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created on 2015-05-04.
 * camp
 *
 * @author 小昊
 * @since 1.0.0
 */
public class ImageFileFilter extends FileFilter {
    /**
     * 文件后缀名
     */
    String ext;

    public ImageFileFilter(String ext) {
        this.ext = ext.toLowerCase();
    }

    /**
     * 指定过滤器接受的文件后缀名
     * @param file  要判断的文件
     * @return      文件的后缀名是否被允许
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        if (index > 0 && index < fileName.length() - 1) {
            String extension = fileName.substring(index + 1).toLowerCase();
            if (extension.equals(ext))
                return true;
        }
        return false;
    }

    /**
     * 获取文件过滤器的描述.(下拉选择框时显示)
     * @return  文件后缀名的描述
     */
    @Override
    public String getDescription() {
        if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("jpe") || ext.equals("jfif"))
            return "JPEG(*.jpg;*.jpeg;*.jpe;*.jfif)";
        if (ext.equals("png"))
            return "PNG(*.png)";
        if (ext.equals("bmp"))
            return "BMP(*.bmp)";
        return null;
    }
}
