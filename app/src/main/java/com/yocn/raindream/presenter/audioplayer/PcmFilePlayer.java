package com.yocn.raindream.presenter.audioplayer;

import android.media.AudioFormat;
import android.os.Message;

import com.yocn.raindream.base.RApplication;
import com.yocn.raindream.base.RecoderConfig;
import com.yocn.raindream.utils.FileUtils;
import com.yocn.raindream.utils.LogUtil;

import java.io.File;
import java.util.Arrays;

/**
 * @ClassName
 * @Description
 * @Author danny
 * @Date 16/6/27 16:44
 */
public class PcmFilePlayer extends BaseMessageLoop {
    protected final String TAG = "PcmFilePlayer";

    private static boolean isPlayerRunning = false;

    private static final int RENDER_START = 1;
    private static final int RENDER_RUN = 2;
    private static final int RENDER_STOP = 3;
    private static final int RENDER_PAUSE = 4;
    private static final int RENDER_SEEK = 5;
    private static final int RENDER_PREPARE = 6;

    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PREPARING = 3;
    private static final int STATE_PREPARED = 4;
    private static final int STATE_PAUSED = 5;

    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    private AudioTrackPlayer mAudioTrack;
    private AudioFileReader mReader;
    private int mByteCount;
    private String mAudioFile;

    private OnPreparedListener mOnPreparedListener;
    private OnCompletionListener mOnCompletionListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnErrorListener mOnErrorListener;
    private OnFramePlayListener mOnFramePlayListener;

    private byte[] mData;
    private int mBeginPosition = 0;
    private int mCurrentPosition = 0;

    private int mSampleRateInHz = RecoderConfig.SampleRate;
    private int mChannels = RecoderConfig.ChannelCount;
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;

    private int mSeekTimeStampMs;
    private int mBytesOfOneSecond; //1秒钟PCM对应的字节数
    private int mFrameRate = 25; //帧率
    private int mBufferSize = 0;
    private int mCustomBufferSize = -1;

    private boolean mIsLoop = true;

    public PcmFilePlayer() {
        super(RApplication.getAppContext(), "PcmFilePlayer");
        mAudioTrack = new AudioTrackPlayer();
        mReader = new MemoryStreamReader();
    }

    public void setCustomBufferSize(int value) {
        this.mCustomBufferSize = value;
    }

    public void setDataSource(String path) {
        LogUtil.i(TAG, "open File : " + path);
        mAudioFile = path;
    }

    public void setLooping(boolean flag) {
        mIsLoop = flag;
    }

    public void setSampleRate(int value) {
        mSampleRateInHz = value;
        mAudioTrack.setSampleRateInHz(value);
    }

    public void setChannels(int value) {
        mChannels = value;
        mAudioTrack.setChannels(value);
    }

    public void setAudioFormat(int value) {
        mAudioFormat = value;
        mAudioTrack.setAudioFormat(value);
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
        mOnPreparedListener = listener;
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        mOnCompletionListener = listener;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        mOnSeekCompleteListener = listener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        mOnErrorListener = listener;
    }

    public void setOnFramePlayListener(OnFramePlayListener mOnFramePlayListener) {
        this.mOnFramePlayListener = mOnFramePlayListener;
    }

    public double getDuration() {
        return mByteCount * 1.0 / mBytesOfOneSecond;
    }

    @Override
    protected boolean recvHandleMessage(Message msg) {
        if (msg.what != RENDER_RUN) {
            LogUtil.v(TAG, "recvHandleMessage msg.what:" + msg.what + "  CurrentState: " + mCurrentState);
        }
        switch (msg.what) {
            case RENDER_RUN:
                mCurrentState = STATE_PLAYING;
                onRun();
                break;
            case RENDER_PREPARE:
                if (mAudioTrack.initAudioPlayer() < 0 || mReader.openFile(mAudioFile) < 0) {
                    onError(new Exception("init audio track failed!"));
                    break;
                }
                try {
                    byte[] contents = FileUtils.readFileToBytes(new File(mAudioFile));
                    mReader.setBytes(contents);
                    mByteCount = contents.length;
                } catch (Exception e) {
                    onError(e);
                    break;
                }

                mBeginPosition = 0;

                adapterParams();

                if (mAudioFile != null && FileUtils.getExtension(mAudioFile).equals("wav")) {
                    mBeginPosition = RecoderConfig.WavHeaderLength * 2;
                }
                mCurrentPosition = mBeginPosition;

                mCurrentState = STATE_PREPARED;
                if (mOnPreparedListener != null) {
                    mOnPreparedListener.onPrepared();
                }

                if (mTargetState == STATE_PLAYING) {
                    start();
                }
                break;
            case RENDER_START:
                removeMessages(RENDER_PAUSE);
                sendEmptyMessage(RENDER_RUN);
                break;
            case RENDER_PAUSE:
                removeMessages(RENDER_RUN);
                if (mSeekTimeStampMs >= 0) {
                    mCurrentPosition = (int) (mBeginPosition + mBytesOfOneSecond * mSeekTimeStampMs * 1.0 / 1000);
                }
                mCurrentState = STATE_PAUSED;
                mTargetState = STATE_PAUSED;

                if (mOnSeekCompleteListener != null) {
                    mOnSeekCompleteListener.onSeekComplete(mSeekTimeStampMs * 1.0f / 1000);
                }
                break;
            case RENDER_STOP:
                removeMessages(RENDER_RUN);
                removeMessages(RENDER_SEEK);
                mAudioTrack.releaseAudioPlayer();
                mReader.closeFile();
                mReader.release();
                mCurrentState = STATE_IDLE;
                mTargetState = STATE_IDLE;
                Quit();
                isPlayerRunning = false;
                break;
            default:
        }

        if (msg.what != RENDER_RUN) {
            LogUtil.v(TAG, "recvHandleMessage msg.what:" + msg.what + "  CurrentState: " + mCurrentState + " ==end");
        }
        return true;
    }

    public boolean isPlaying() {
        if (mCurrentState == STATE_PLAYING) {
            return true;
        } else {
            return false;
        }
    }

    private void adapterParams() {
        if (mCustomBufferSize > 0) {
            mBufferSize = mCustomBufferSize;
            return;
        }

        int bitNum = mAudioFormat == AudioFormat.ENCODING_PCM_16BIT ? 16 : 8;

        //计算1秒钟对应PCM数据字节数
        mBytesOfOneSecond = mSampleRateInHz * bitNum * mChannels / 8;

        //计算每帧写入的PCM buffer字节数
        mBufferSize = mBytesOfOneSecond / mFrameRate;
        //双声道必须为偶数，如果是奇数则+1
        if (mBufferSize % 2 == 1) {
            mBufferSize++;
        }
    }

    public void setFPS(int fps) {
        mFrameRate = fps;
//        adapterParams();
    }

    //发生异常
    private void onError(Exception e) {
        if (mOnErrorListener != null) {
            mOnErrorListener.onError(e);
        }
        mCurrentState = STATE_ERROR;
        mTargetState = STATE_ERROR;
        sendEmptyMessage(RENDER_STOP);
    }

    private void onRun() {
        if (mData == null) {
            mData = new byte[mBufferSize];
        }

        Arrays.fill(mData, (byte) 0);
        int sizeInBytes = mReader.read(mData, 0, mData.length, mCurrentPosition);

        if (sizeInBytes > 0) {
            if (mOnFramePlayListener != null) {
                int playBytes = mCurrentPosition - mBeginPosition;
                mOnFramePlayListener.onFramePlay(playBytes * 1.0f / mBytesOfOneSecond, mData, sizeInBytes);
            }

            if (mAudioTrack != null) {
                if (sizeInBytes > 0) {
                    mAudioTrack.write(mData, 0, sizeInBytes);
                }
                mCurrentPosition += sizeInBytes;
            }

            sendEmptyMessage(RENDER_RUN);
        } else {
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion();
            }

            if (mIsLoop) {
                mCurrentPosition = mBeginPosition;
                sendEmptyMessage(RENDER_RUN);
            } else {
                sendEmptyMessage(RENDER_STOP);
            }
        }
    }

    public void prepare() {
        if (mCurrentState == STATE_IDLE || mCurrentState == STATE_ERROR) {
            Run();
            mCurrentState = STATE_PREPARING;
            sendEmptyMessage(RENDER_PREPARE);
            isPlayerRunning = true;
        }
    }

    public void start() {
        if (!isPlayerRunning) {
            prepare();
        }
        if (mCurrentState == STATE_PAUSED || mCurrentState == STATE_PREPARED) {
            sendEmptyMessage(RENDER_RUN);
        }
        mTargetState = STATE_PLAYING;
    }

    public void stop() {
        if (mCurrentState != STATE_IDLE) {
            removeMessages(RENDER_RUN);
            sendEmptyMessage(RENDER_STOP);
        }
        mTargetState = STATE_IDLE;
    }

    public void pause() {
        if (mCurrentState == STATE_PLAYING) {
            sendEmptyMessage(RENDER_PAUSE);
        }
        mTargetState = STATE_PAUSED;
    }

    public void setVolume(int volume) {
        mAudioTrack.setVolume(volume);
    }

    public void seekTo(int msec) {
        if (!isPlayerRunning) {
            prepare();
        }
        mSeekTimeStampMs = msec;
        sendEmptyMessage(RENDER_SEEK);
    }

    public interface OnPreparedListener {
        void onPrepared();
    }

    public interface OnCompletionListener {
        void onCompletion();
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(float sec);
    }

    public interface OnErrorListener {
        boolean onError(Exception e);
    }

    public interface OnFramePlayListener {
        void onFramePlay(float sec, byte[] data, int size);
    }
}
