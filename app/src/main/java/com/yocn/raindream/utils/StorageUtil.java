package com.yocn.raindream.utils;

import com.yocn.raindream.base.RApplication;

import java.io.File;

public class StorageUtil {
    private final static String TAG = "StorageUtil";
    private final static String OGG = "ogg";
    private final static String WAV = "wav";
    private final static String W = "w";

    public static String getExternalFilesDir() {
        File file = RApplication.getAppContext().getExternalFilesDir("");
        if (file == null) {
            return "";
        } else {
            return file.getAbsolutePath();
        }
    }

    public static String getOggPath() {
        String oggPath = getExternalFilesDir() + "/" + OGG;
        FileUtils.checkDir(oggPath);
        return oggPath;
    }

    public static String getWavPath() {
        String oggPath = getExternalFilesDir() + "/" + WAV;
        FileUtils.checkDir(oggPath);
        return oggPath;
    }

    public static String getWPath() {
        String oggPath = getExternalFilesDir() + "/" + W;
        FileUtils.checkDir(oggPath);
        return oggPath;
    }

}