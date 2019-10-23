package com.yocn.raindream.presenter.audioplayer;

/**
 * @ClassName
 * @Description
 * @Author danny
 * @Date 16/6/27 16:46
 */
public abstract class AudioFileReader {
    protected String TAG = "AudioFileReader";

    abstract public int openFile(String filepath);

    abstract public int setBytes(byte[] stream);

    abstract public boolean isFileOverFlow(int pos, long pts);

    abstract public int read(byte[] dst, int dstOffset, int byteCount, int pos);

    abstract public int closeFile();

    abstract public int release();
}
