package com.yocn.raindream.presenter.audioplayer;

import com.yocn.raindream.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class FileHelper extends BaseCommonHelper {
    private static FileHelper instance = new FileHelper();

    public static FileHelper sharedPreferences() {
        return instance;
    }

    public boolean ExistsFilePath(String pathString) { // path is exists or not
        if (CheckStringIsNULL(pathString)) {
            return false;
        }
        return new File(pathString).exists();
    }

    public long getFileSize(String pathString) { // path is exists or not
        if (CheckStringIsNULL(pathString)) {
            return 0;
        }
        return new File(pathString).length();
    }

    public RandomAccessFile OpenFile(String pathString, boolean append) { // open
        // file
        RandomAccessFile raf = null;
        File file = null;
        if (!CheckStringIsNULL(pathString)) {
            try {
                if (ExistsFilePath(pathString)) {
                    file = new File(pathString);
                } else {
                    file = new File(pathString);
                }
                if (!append) {
                    OutputStream outputStream = new FileOutputStream(file,
                            append);
                    outputStream.close();
                }
                raf = new RandomAccessFile(file, "rw");
            } catch (FileNotFoundException e) {
                LogUtil.e("FileHelper", e);
                raf = null;
            } catch (IOException e) {
                LogUtil.e("FileHelper", e);
            }
        }
        return raf;
    }

    public boolean Mkdir(String strDirString) {
        boolean result = false;
        if (!CheckStringIsNULL(strDirString) && !ExistsFilePath(strDirString)) {
            File file = new File(strDirString);
            result = file.mkdirs();
        }
        return result;
    }

}
