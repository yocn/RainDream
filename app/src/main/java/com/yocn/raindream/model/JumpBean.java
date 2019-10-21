package com.yocn.raindream.model;

import com.yocn.raindream.R;

import java.io.Serializable;

/**
 * @Author yocn
 * @Date 2019/8/4 8:49 PM
 * @ClassName JumpBean
 */
public class JumpBean implements Serializable {
    private String show;
    private Class toClass;
    private int color = R.color.color1;
    private int textColor = R.color.write;

    public JumpBean(String show, Class toClass, int color, int textColor) {
        this.show = show;
        this.toClass = toClass;
        this.color = color;
        this.textColor = textColor;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public Class getToClass() {
        return toClass;
    }

    public void setToClass(Class toClass) {
        this.toClass = toClass;
    }
}
