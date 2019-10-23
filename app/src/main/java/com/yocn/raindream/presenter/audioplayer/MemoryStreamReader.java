package com.yocn.raindream.presenter.audioplayer;

import java.nio.BufferUnderflowException;

/**
 * @ClassName
 * @Description
 * @Author danny
 * @Date 16/6/30 13:51
 */
public class MemoryStreamReader extends AudioFileReader {
    protected final String TAG = "MemoryStreamReader";

    SeekByteArrayInputStream mStream;

    public MemoryStreamReader() {
    }

    @Override
    public int openFile(String filepath) {
        return 0;
    }

    public int setBytes(byte[] stream) {
        mStream = new SeekByteArrayInputStream(stream);
        return 0;
    }

    @Override
    public boolean isFileOverFlow(int pos, long pts) {
        return false;
    }

    @Override
    public int read(byte[] dst, int dstOffset, int byteCount, int pos) {
        if (mStream == null) {
            return -1;
        }
        long currPostion = -1;
        try {
            if (pos != -1) {
                mStream.seek(pos);
            }
            currPostion = mStream.getPosition();
//            LogUtil.v(TAG, byteCount + "    " +  pos + "    " + currPostion);
            if (0 >= mStream.read(dst, dstOffset, byteCount)) return 0;
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
            return 0;
        } catch (IndexOutOfBoundsException e) {
//            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return (int) (mStream.getPosition() - currPostion);
    }

    @Override
    public int closeFile() {
        return 0;
    }

    @Override
    public int release() {
        if (mStream != null) {
            mStream.close();
        }
        return 0;
    }
}
