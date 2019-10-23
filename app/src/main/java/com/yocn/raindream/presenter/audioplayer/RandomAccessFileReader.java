package com.yocn.raindream.presenter.audioplayer;

import com.yocn.raindream.utils.LogUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;

/**
 * @ClassName
 * @Description
 * @Author danny
 * @Date 16/6/27 20:27
 */
public class RandomAccessFileReader extends AudioFileReader {
    protected final String TAG = "WavFileReader";
    private long mFileLength = 0;
    private RandomAccessFile mPcmFile;

    public RandomAccessFileReader() {

    }

    public void setBufferSize(int size) {
    }

    public int openFile(String filepath) {
        mPcmFile = FileHelper.sharedPreferences().OpenFile(
                filepath, true);
        if (mPcmFile == null) return -1;
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
        if (mPcmFile != null) {
            try {
                return (int) mPcmFile.getFilePointer();
            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e(TAG, e);
            }
        }

        return -1;
    }

    public int read(byte[] dst, int dstOffset, int byteCount, int pos) {
        long currPostion = -1;
        try {
            if (pos != -1) {
                mPcmFile.seek(pos);
            }
            currPostion = mPcmFile.getFilePointer();
            LogUtil.v(TAG, byteCount + "    " +  pos + "    " + currPostion);
            mPcmFile.read(dst,dstOffset,byteCount);
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

        try {
            return (int) (mPcmFile.getFilePointer() - currPostion);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, e);
        }
        return -1;
    }


    public int closeFile() {
        return 0;
    }

    @Override
    public int release() {
        if (mPcmFile != null)
            try {
                mPcmFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return 0;
    }
}
