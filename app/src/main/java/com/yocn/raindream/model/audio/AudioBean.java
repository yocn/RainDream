package com.yocn.raindream.model.audio;

/**
 * @Author yocn
 * @Date 2019-10-18 10:57
 * @ClassName AudioBean
 */
public class AudioBean {
    private int id;
    private String effect;
    private String path;
    private String name;

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
                '}';
    }
}
