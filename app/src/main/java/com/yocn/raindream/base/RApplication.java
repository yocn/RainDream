package com.yocn.raindream.base;

import android.app.Application;

import com.yocn.raindream.presenter.audio.AudioDataManager;
import com.yocn.raindream.presenter.audio.MAudioManager;
import com.yocn.raindream.utils.FileUtils;
import com.yocn.raindream.utils.LogUtil;
import com.yocn.raindream.utils.StorageUtil;

import java.util.ArrayList;
import java.util.List;

import raindream.yocn.nativelib.NativeJNI;

/**
 * @Author yocn
 * @Date 2019-10-17 20:45
 * @ClassName RApplication
 */
public class RApplication extends Application {
    static RApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    public void init() {
        String path = StorageUtil.getOggPath();
//        String path = "mnt/sdcard/Android/data/com.yocn.raindream/files/ogg";
        FileUtils.copyAssetsForder2SDCard(this, "oggs", path);
        LogUtil.d("path->" + path);
        AudioDataManager.initAudioList();
        //ffmpeg -i $1/$filename -acodec pcm_s16le -ac 2 -ar 44100 wav2/$name.wav
        String wPath = StorageUtil.getWPath();
        List<String> list = new ArrayList<>();
        list.add("ffmpeg");
        list.add("-i");
        list.add(path + "/wind.ogg");
        list.add(path + "/guitar.ogg");
        list.add("-acodec");
        list.add("pcm_s16le");
        list.add("-ac");
        list.add("2");
        list.add("-ar");
        list.add("44100");
        list.add(wPath + "/guitar.wav");
        Object[] ss = list.toArray();
        NativeJNI.helloFFmpeg();

        NativeJNI.execCmd(ss);
        LogUtil.d("path2->" + path);
    }

    public static RApplication getAppContext() {
        return mInstance;
    }
}
