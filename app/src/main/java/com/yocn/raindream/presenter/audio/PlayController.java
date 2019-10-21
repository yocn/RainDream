package com.yocn.raindream.presenter.audio;

import android.media.SoundPool;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.yocn.raindream.base.RApplication;
import com.yocn.raindream.model.audio.AudioBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author yocn
 * @Date 2019-10-21 20:43
 * @ClassName PlayController
 */
public class PlayController {

    private static PlayController mInstance;
    private SoundPool mSoundPool;
    private SparseIntArray mAudioPool = new SparseIntArray();
    private Set<Integer> mPlaying = new HashSet<>();

    public static PlayController getInstance() {
        synchronized (PlayController.class) {
            if (mInstance == null) {
                synchronized (PlayController.class) {
                    mInstance = new PlayController();
                }
            }
        }
        return mInstance;
    }

    private PlayController() {
        init();
    }

    private void init() {
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(10);
        //转换音频格式
        spb.setAudioAttributes(null);
        //创建SoundPool对象
        mSoundPool = spb.build();
    }

    public void initAudioPool(AudioBean bean) {
        try {
            mAudioPool.append(bean.getId(), mSoundPool.load(RApplication.getAppContext().getAssets().openFd(bean.getName()), 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * soundID：Load()返回的声音ID号，以上可以通过map.get(1)获取
     * leftVolume：左声道音量设置  一般为0－1，默认填1
     * rightVolume：右声道音量设置 一般为0－1，默认填1
     * priority：指定播放声音的优先级，数值越高，优先级越大。默认填0
     * loop：指定是否循环：-1表示无限循环，0表示不循环，其他值表示要重复播放的次数
     * rate：指定播放速率：1.0的播放率可以使声音按照其原始频率，而2.0的播放速率，可以使声音按照其 原始频率的两倍播放。如果为0.5的播放率，则播放速率是原始频率的一半。播放速率的取值范围是0.5至2.0。
     *
     * @param id soundID
     */
    public void play(int id) {
        mSoundPool.play(mAudioPool.get(id), 1, 1, 0, -1, 1);
        mPlaying.add(id);
    }

    public void clearPlay() {
        for (int soundId : mPlaying) {
            mSoundPool.stop(soundId);
        }
    }
}
