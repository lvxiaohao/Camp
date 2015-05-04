package cn.lxhao.swing.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 这个类可以实例化一个输入框对象，
 * 这个输入框默认会显示一些文字，而这些文字从构造方法传入。<br>
 * 当输入框获得焦点时，框内不再显示内容，此时与JTextField相同；<br>
 * 当输入框失去焦点时，如果输入框内有字符，则没有任何反应，<br>
 * 如果此时输入框内没有输入内容，则它会重新显示开始定义的默认文本
 * Created on 2015-05-04.
 *
 * @author 小昊
 * @since 1.0.0
 */
public class PromptingTextField extends JTextField implements FocusListener, KeyListener {
    /**
     * 标记是否已经输入内容,true为已输入,false为未输入
     */
    private boolean hasInput = false;
    /**
     * 该文本框默认显示的内容
     */
    private final String defaultText;
    /**
     * 默认显示内容的颜色
     */
    private Color suspendColor = new Color(180, 180, 180);
    /**
     * 显示正常文字的颜色
     */
    private Color normalColor = new Color(0, 0, 0);

    public PromptingTextField(String text) {
        this.addFocusListener(this);
        this.addKeyListener(this);
        this.defaultText = text;
        this.setForeground(suspendColor);
        this.setText(text);
    }

    /**
     * 清除文本,显示默认字符串
     */
    public void clearText() {
        this.setForeground(suspendColor);
        this.setText(defaultText);
    }

    /**
     * 获得输入标记
     *
     * @return flag标记的值
     */
    public boolean hasInput() {
        return hasInput;
    }

    /**
     * 获得焦点时如果没有输入内容,清空内容,更改颜色
     */
    @Override
    public void focusGained(FocusEvent e) {
        if (!hasInput) {
            this.setText("");
            this.setForeground(normalColor);
        }
    }

    /**
     * 失去焦点时,如果文本框中的内容长度还为0,则说明没有输入内容,改颜色,设置默认内容,更改标记
     */
    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().length() == 0) {
            this.setForeground(suspendColor);
            this.setText(defaultText);
            hasInput = false;
        }
    }

    public void keyTyped(KeyEvent e) {
        //在文本框中按下按钮,则标记为已输入
        hasInput = true;
    }

    public void keyPressed(KeyEvent e) {
        // do nothing
    }

    public void keyReleased(KeyEvent e) {
        // do nothing
    }

    public String getDefaultText() {
        return defaultText;
    }

    public Color getSuspendColor() {
        return suspendColor;
    }

    public void setSuspendColor(Color suspendColor) {
        this.suspendColor = suspendColor;
    }

    public Color getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(Color normalColor) {
        this.normalColor = normalColor;
    }
}
