package com.yocn.raindream.presenter.audioplayer;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.yocn.raindream.base.RecoderConfig;
import com.yocn.raindream.utils.LogUtil;


public class AudioPlayer {
    private final static String TAG = "AudioPlayer";
    private AudioTrack mAudioTrack = null;

    public static final int PLAY_MODE_AUTO = 0;        // 自动模式
    public static final int PLAY_MODE_RECEIVER = 1;        // 听筒播放模式
    public static final int PLAY_MODE_SPEAKER = 2;        // 喇叭播放模式

    private PlayAudioThread mPlayAudioThread = null;        // 播放线程
    private boolean mPlayThreadExitFlag = false;            // 播放线程退出标志
    private int mMinPlayBufSize = 0;
    private boolean mAudioPlayReleased = false;

    private Context mContext = null;
    private int mPlayMode = PLAY_MODE_SPEAKER;                // 默认是喇叭播放模式

    // 设置父窗口句柄
    public void SetContext(Context ctx) {
        mContext = ctx;
    }

    // 初始化音频播放器
    public int InitAudioPlayer() {
        if (mAudioTrack != null)
            return 0;

        int channel, samplerate, samplebit;
        samplerate = RecoderConfig.SampleRate;
        channel = RecoderConfig.ChannelCount == 1 ? AudioFormat.CHANNEL_CONFIGURATION_MONO
                : AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        samplebit = AudioFormat.ENCODING_PCM_16BIT;

        try {
            mAudioPlayReleased = false;
            // 获得构建对象的最小缓冲区大小
            mMinPlayBufSize = AudioTrack.getMinBufferSize(samplerate, channel, samplebit);
            mAudioTrack = new AudioTrack(mPlayMode == PLAY_MODE_SPEAKER ? AudioManager.STREAM_MUSIC : AudioManager.STREAM_VOICE_CALL,
                    samplerate, channel, samplebit, mMinPlayBufSize, AudioTrack.MODE_STREAM);

            if (mPlayAudioThread == null) {
                mPlayThreadExitFlag = false;
                mPlayAudioThread = new PlayAudioThread();
                mPlayAudioThread.start();
            }
            LogUtil.d(TAG, "mMinPlayBufSize = " + mMinPlayBufSize);
        } catch (Exception e) {
            return -1;
        }
        return 0;
    }

    public void ReleaseAudioPlayer() {
        if (mAudioPlayReleased)
            return;
        mAudioPlayReleased = true;
        LogUtil.d(TAG, "releaseAudioPlayer");
        if (mPlayAudioThread != null) {
            mPlayThreadExitFlag = true;
            mPlayAudioThread = null;
        }

        if (mAudioTrack != null) {
            try {
                mAudioTrack.stop();
                mAudioTrack.release();
                mAudioTrack = null;
            } catch (Exception e) {

            }
        }
    }

    class PlayAudioThread extends Thread {
        @Override
        public void run() {
            if (mAudioTrack == null)
                return;
            try {
                android.os.Process.setThreadPriority(
                        android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            } catch (Exception e) {
                LogUtil.d(TAG, "Set play thread priority failed: " + e.getMessage());
            }
            try {
                mAudioTrack.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
            LogUtil.d(TAG, "audio play....");
            byte[] data = null;
            while (!mPlayThreadExitFlag) {
                try {
                    //byte[] data = read(...);
                    mAudioTrack.write(data, 0, data.length);
                } catch (Exception e) {
                    break;
                }
            }
            LogUtil.d(TAG, "audio play stop....");
        }
    }

    // 判断当前是否为喇叭播放模式
    public Boolean IsSpeakerMode() {
        return mPlayMode == PLAY_MODE_SPEAKER;
    }

    // 切换声音播放设备（喇叭、耳机）
    public void SwitchPlayMode(int mode) {
        try {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            if (mode == PLAY_MODE_AUTO) {
                if (IsSpeakerMode()) {
                    audioManager.setMode(AudioManager.MODE_IN_CALL);        // 切换到听筒
                    mPlayMode = PLAY_MODE_RECEIVER;
                } else {
                    audioManager.setMode(AudioManager.MODE_NORMAL);            // 切换到扬声器
                    mPlayMode = PLAY_MODE_SPEAKER;
                }
            } else if (mode == PLAY_MODE_RECEIVER) {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                mPlayMode = PLAY_MODE_RECEIVER;
            } else if (mode == PLAY_MODE_SPEAKER) {
                audioManager.setMode(AudioManager.MODE_NORMAL);
                mPlayMode = PLAY_MODE_SPEAKER;
            }
            // 重新初始化播放器
            ReleaseAudioPlayer();
            InitAudioPlayer();
        } catch (Exception e) {
        }
    }
}
