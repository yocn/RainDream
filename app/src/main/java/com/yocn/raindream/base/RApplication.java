package com.yocn.raindream.base;

import android.app.Application;

import com.yocn.raindream.presenter.audio.MAudioManager;
import com.yocn.raindream.utils.FileUtils;
import com.yocn.raindream.utils.LogUtil;
import com.yocn.raindream.utils.StorageUtil;

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
        FileUtils.copyAssetsForder2SDCard(this, "oggs", path);
        LogUtil.d("path->" + path);
        MAudioManager.getInstance().initAudioList();
    }

    public static RApplication getAppContext() {
        return mInstance;
    }
}
