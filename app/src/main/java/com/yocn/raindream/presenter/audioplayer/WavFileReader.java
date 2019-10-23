package com.yocn.raindream.presenter.audioplayer;

import com.yocn.raindream.utils.LogUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName
 * @Description
 * @Author danny
 * @Date 16/6/27 20:27
 */
public class WavFileReader extends AudioFileReader {
    protected final String TAG = "WavFileReader";
    private long mFileLength = 0;
    private MappedByteBuffer mFileMap = null;
    private FileInputStream mFis;
    private FileChannel mFc;

    public WavFileReader() {
    }

    public void setBufferSize(int size) {
    }

    public int openFile(String filepath) {
        try {
            if (filepath == null || filepath.length() == 0) return -1;

            mFis = new FileInputStream(filepath);
            mFc = mFis.getChannel();
            mFileLength = mFc.size();
            mFileMap = mFc.map(FileChannel.MapMode.READ_ONLY, 0, mFileLength);

            mFis.close();
            mFis = null;

            mFc.close();
            mFc = null;

        } catch (IOException e) {
            e.printStackTrace();
            closeFile();
            return -1;
        }

        return 0;
    }

    @Override
    public int setBytes(byte[] stream) {
        return 0;
    }

    public long getFileLength() {
        return mFileLength;
    }

    @Override
    public boolean isFileOverFlow(int pos, long pts) {
        return pos >= getFileLength();
    }

    public int getPosition() {
        if (mFileMap != null)
            return mFileMap.position();
        else
            return -1;
    }

    public int read(byte[] dst, int dstOffset, int byteCount, int pos) {
        int currPostion = -1;
        try {
            if (pos != -1) {
                mFileMap.position(pos);
            }
            currPostion = mFileMap.position();
            LogUtil.v(TAG, byteCount + "    " +  pos + "    " + currPostion);
            mFileMap.get(dst, dstOffset, byteCount);
        } catch (BufferUnderflowException e) {
            e.printStackTrace();
            return 0;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return mFileMap.position() - currPostion;
    }


    public int closeFile() {
        return 0;
    }

    @Override
    public int release() {
        mFileMap = null;

        try {
            if (mFis != null) {
                mFis.close();
                mFis = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (mFc != null) {
                mFc.close();
                mFc = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
