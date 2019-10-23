package com.yocn.raindream.presenter.audio;

import com.yocn.raindream.base.RecoderConfig;
import com.yocn.raindream.model.audio.AudioBean;
import com.yocn.raindream.presenter.audioplayer.PcmFilePlayer;
import com.yocn.raindream.utils.FileUtils;
import com.yocn.raindream.utils.LogUtil;
import com.yocn.raindream.utils.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yocn
 * @Date 2019-10-18 10:54
 * @ClassName MAudioManager
 */
public class MAudioManager {
    private static MAudioManager mInstance;
    private List<AudioBean> mAudioList = new ArrayList<>();
    private boolean mIsInit = false;

    private String mWavFile;
    private PcmFilePlayer mPcmFilePlayer;

    public static MAudioManager getInstance() {
        synchronized (MAudioManager.class) {
            if (mInstance == null) {
                synchronized (MAudioManager.class) {
                    mInstance = new MAudioManager();
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

        for (int i = 0; i < files.length; ++i) {
            AudioBean audioBean = new AudioBean();
            audioBean.setName(files[i].getName());
            audioBean.setPath(files[i].getAbsolutePath());
            audioBean.setId(i);
            mAudioList.add(audioBean);
            PlayController.getInstance().initAudioPool(audioBean);
        }
    }

    public void initPlayer(String name) {
        mWavFile = StorageUtil.getWavPath() + "/" + name;
        //guitar.wav
        mPcmFilePlayer = new PcmFilePlayer();

        mPcmFilePlayer.setDataSource(mWavFile);
        mPcmFilePlayer.setCustomBufferSize(RecoderConfig.RecBufSize);
        mPcmFilePlayer.prepare();
        mPcmFilePlayer.setOnFramePlayListener((sec, data, size) -> {
            LogUtil.d("sec:" + sec + " data:" + data.length + " size:" + size);
        });
        mIsInit = true;
    }


    public void start() {
        if (mIsInit) {
            mPcmFilePlayer.start();
        }
    }

    public void pause() {
        if (mIsInit) {
            mPcmFilePlayer.pause();
        }
    }

    public void stop() {
        mIsInit = false;
        if (mPcmFilePlayer != null) {
            mPcmFilePlayer.stop();
            mPcmFilePlayer = null;
        }
    }

}
