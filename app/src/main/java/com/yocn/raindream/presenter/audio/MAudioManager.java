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

    private List<PcmFilePlayer> players = new ArrayList<>();

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

    public void initAPlayer(String name) {
        PcmFilePlayer player = initPlayer(name);
        players.add(player);
    }

    private PcmFilePlayer initPlayer(String name) {
        String mWavFile = StorageUtil.getWavPath() + "/" + name;
        //guitar.wav
        PcmFilePlayer pcmFilePlayer = new PcmFilePlayer();

        pcmFilePlayer.setDataSource(mWavFile);
        pcmFilePlayer.setCustomBufferSize(RecoderConfig.RecBufSize);
        pcmFilePlayer.prepare();
        pcmFilePlayer.setOnFramePlayListener((sec, data, size) -> {
//            LogUtil.d("sec:" + sec + " data:" + data.length + " size:" + size);
        });
        return pcmFilePlayer;
    }

    public void start() {
        for (PcmFilePlayer pcmFilePlayer : players) {
            pcmFilePlayer.start();
        }
    }

    public void pause() {
        for (PcmFilePlayer pcmFilePlayer : players) {
            pcmFilePlayer.pause();
        }
    }

    public void stop() {
        for (PcmFilePlayer pcmFilePlayer : players) {
            pcmFilePlayer.stop();
            pcmFilePlayer = null;
        }
    }

    public void setVolume(int index, int volume) {
        if (index >= players.size()) {
            return;
        }
        LogUtil.d("index->" + index + " v:" + volume);
        players.get(index).setVolume(volume);
    }

    public void setAllVolume(int volume) {
        for (int i = 0; i < players.size(); ++i) {
            setVolume(i, volume);
        }
    }

}
