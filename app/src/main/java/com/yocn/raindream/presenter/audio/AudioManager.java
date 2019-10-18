package com.yocn.raindream.presenter.audio;

import com.yocn.raindream.model.audio.AudioBean;
import com.yocn.raindream.utils.FileUtils;
import com.yocn.raindream.utils.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yocn
 * @Date 2019-10-18 10:54
 * @ClassName AudioManager
 */
public class AudioManager {
    private static AudioManager mInstance;
    private List<AudioBean> mAudioList = new ArrayList<>();

    public static AudioManager getInstance() {
        synchronized (AudioManager.class) {
            if (mInstance == null) {
                synchronized (AudioManager.class) {
                    mInstance = new AudioManager();
                }
            }
        }
        return mInstance;
    }

    public void initAudioList() {
        String rootPath = StorageUtil.getOggPath();
        File[] files = FileUtils.listFiles(rootPath);
        if (files == null) {
            return;
        }
        for (File file : files) {
            AudioBean audioBean = new AudioBean();
            audioBean.setName(file.getName());
            audioBean.setPath(file.getAbsolutePath());
            mAudioList.add(audioBean);
        }
    }

}
