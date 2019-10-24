package com.yocn.raindream.presenter.audioplayer;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import com.yocn.raindream.base.RApplication;
import com.yocn.raindream.base.RecoderConfig;
import com.yocn.raindream.utils.LogUtil;


/**
 * @ClassName
 * @Description
 * @Author danny
 * @Date 16/6/27 16:44
 */
public class AudioTrackPlayer {
    private final static String TAG = "AudioTrackPlayer";
    private AudioTrack mAudioTrack = null;
    // 自动模式
    public static final int PLAY_MODE_AUTO = 0;
    // 听筒播放模式
    public static final int PLAY_MODE_RECEIVER = 1;
    // 喇叭播放模式
    public static final int PLAY_MODE_SPEAKER = 2;

    private boolean mAudioPlayReleased = false;

    private Context mContext = RApplication.getAppContext();
    // 默认是喇叭播放模式
    private int mPlayMode = PLAY_MODE_SPEAKER;

    private int mBufferSize = 0;
    private float mCurrVolume = 1f;

    private int mSampleRateInHz = RecoderConfig.SampleRate;
    private int mChannels = RecoderConfig.ChannelCount;
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;

    public void setContext(Context ctx) {
        mContext = ctx;
    }

    public int getBufferSize() {
        return mBufferSize;
    }

    public void setSampleRateInHz(int mSampleRateInHz) {
        this.mSampleRateInHz = mSampleRateInHz;
    }

    public void setChannels(int mChannels) {
        this.mChannels = mChannels;
    }

    public void setAudioFormat(int mAudioFormat) {
        this.mAudioFormat = mAudioFormat;
    }

    // 初始化音频播放器
    public int initAudioPlayer() {
        if (mAudioTrack != null) {
            return 0;
        }

        int channel = mChannels == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;
        try {
            mAudioPlayReleased = false;
            int minBufSize = AudioTrack.getMinBufferSize(mSampleRateInHz, channel, mAudioFormat);
            LogUtil.d(TAG, "start mMinPlayBufSize = " + minBufSize);
            mAudioTrack = new AudioTrack(mPlayMode == PLAY_MODE_SPEAKER ? AudioManager.STREAM_MUSIC : AudioManager.STREAM_VOICE_CALL,
                    mSampleRateInHz, channel, AudioFormat.ENCODING_PCM_16BIT, minBufSize, AudioTrack.MODE_STREAM);

            mBufferSize = minBufSize;
            mAudioTrack.setVolume(mCurrVolume);
            mAudioTrack.play();
            LogUtil.d(TAG, "end mMinPlayBufSize = " + minBufSize);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
            return -1;
        }
        return 0;
    }

    public int write(byte[] audioData, int offsetInBytes, int sizeInBytes) {
        try {
            return mAudioTrack.write(audioData, offsetInBytes, sizeInBytes);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return -1;
    }

    public void setVolume(float volume) {
        mCurrVolume = volume / 100;
        if (mAudioTrack != null) {
            mAudioTrack.setVolume(mCurrVolume);
        }
    }

    public void releaseAudioPlayer() {
        if (mAudioPlayReleased) {
            return;
        }
        mAudioPlayReleased = true;
        LogUtil.d(TAG, "releaseAudioPlayer");

        if (mAudioTrack != null) {
            try {
                mAudioTrack.pause();
                mAudioTrack.stop();
                mAudioTrack.release();
                mAudioTrack = null;
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
    }

    // 判断当前是否为喇叭播放模式
    public Boolean isSpeakerMode() {
        return mPlayMode == PLAY_MODE_SPEAKER;
    }

    // 切换声音播放设备（喇叭、耳机）
    public void switchPlayMode(int mode) {
        try {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            if (mode == PLAY_MODE_AUTO) {
                if (isSpeakerMode()) {
                    // 切换到听筒
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    mPlayMode = PLAY_MODE_RECEIVER;
                } else {
                    // 切换到扬声器
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    mPlayMode = PLAY_MODE_SPEAKER;
                }
            } else if (mode == PLAY_MODE_RECEIVER) {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                mPlayMode = PLAY_MODE_RECEIVER;
            } else if (mode == PLAY_MODE_SPEAKER) {
                audioManager.setMode(AudioManager.MODE_NORMAL);
                mPlayMode = PLAY_MODE_SPEAKER;
            }

            releaseAudioPlayer();
            initAudioPlayer();
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
    }
}
