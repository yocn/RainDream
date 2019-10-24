package com.yocn.raindream.model.audio;

import java.io.Serializable;

/**
 * @Author yocn
 * @Date 2019-10-18 10:57
 * @ClassName AudioBean
 */
public class AudioBean implements Serializable {
    //id
    private int id;
    //音效
    private String effect;
    //路径
    private String path;
    //名字
    private String name;
    //图标
    private String icon;
    //音量
    private int volume = 50;

    public AudioBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AudioBean audioBean = (AudioBean) o;

        return id == audioBean.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "AudioBean{" +
                "id=" + id +
                ", effect='" + effect + '\'' +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", volume=" + volume +
                '}';
    }
}
