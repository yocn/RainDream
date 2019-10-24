package com.yocn.raindream.model;

import com.yocn.raindream.R;
import com.yocn.raindream.model.audio.AudioBean;

import java.io.Serializable;
import java.util.List;

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
    private List<AudioBean> audios;

    public JumpBean(String show, Class toClass, int color, int textColor, List<AudioBean> audios) {
        this.show = show;
        this.toClass = toClass;
        this.color = color;
        this.textColor = textColor;
        this.audios = audios;
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

    public List<AudioBean> getAudios() {
        return audios;
    }

    public void setAudios(List<AudioBean> audios) {
        this.audios = audios;
    }
}
