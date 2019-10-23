package com.yocn.raindream.presenter.audioplayer;

import java.io.ByteArrayInputStream;

/**
 * @ClassName
 * @Description
 * @Author danny
 * @Date 16/6/30 14:14
 */
public class SeekByteArrayInputStream extends ByteArrayInputStream {
    protected final String TAG = "SeekByteArrayInputStream";
    public SeekByteArrayInputStream(byte[] buf) {
        super(buf);
    }

    public SeekByteArrayInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }

    public void seek(int pos) {
        if (pos < 0 || pos > count) { throw new IndexOutOfBoundsException("" + pos + ":" + count); }
        this.pos = pos;
    }

    public long getPosition() {
        return pos;
    }

    public void close() {
        buf = null;
        count = 0;
        System.gc();
    }
}
