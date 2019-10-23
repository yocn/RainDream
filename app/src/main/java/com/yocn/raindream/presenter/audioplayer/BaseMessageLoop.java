package com.yocn.raindream.presenter.audioplayer;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import com.yocn.raindream.utils.LogUtil;

/**
 * @ClassName
 * @Description
 * @Author danny
 * @Date 16/3/3 18:15
 */
public abstract class BaseMessageLoop {
    private String TAG = "BaseMessageLoop";

    private volatile MsgHandlerThread mHandlerThread;
    private volatile Handler mHandler;
    private String mName;
    public BaseMessageLoop(Context context, String name) {
        TAG += "_" + name;
        mName = name;
    }

    public MsgHandlerThread getHandlerThread() {
        return mHandlerThread;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void Run() {
        Quit();
        //LogUtil.v(TAG,  mName + " HandlerThread Run");
        synchronized (TAG) {
            mHandlerThread = new MsgHandlerThread(mName);
            mHandlerThread.start();
            mHandler = new Handler(mHandlerThread.getLooper(), mHandlerThread);
        }
    }

    public void Quit() {
        //LogUtil.v(TAG,  mName + " HandlerThread Quit");
        synchronized (TAG) {
            if (mHandlerThread != null) {
                mHandlerThread.quit();
            }

            if (mHandler != null) {
                mHandler.removeCallbacks(mHandlerThread);
            }

            mHandlerThread = null;
            mHandler = null;
        }
    }

    public void sendMessage(int what, int arg1, int arg2) {
        synchronized (TAG) {
            if (mHandler != null) {
                mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2));
            } else {
                LogUtil.v(TAG, "mHandler == null");
            }
        }
    }

    public void sendMessage(int what, int arg1, int arg2, Object obj) {
        synchronized (TAG) {
            if (mHandler != null) {
                mHandler.sendMessage(mHandler.obtainMessage(what, arg1, arg2, obj));
            } else {
                LogUtil.v(TAG, "mHandler == null");
            }
        }
    }

    public void sendMessageDelayed(int what, int arg1, int arg2, Object obj, long delayMillis) {
        synchronized (TAG) {
            if (mHandler != null) {
                mHandler.sendMessageDelayed(mHandler.obtainMessage(what, arg1, arg2, obj), delayMillis);
            } else {
                LogUtil.v(TAG, "mHandler == null");
            }
        }
    }


    public void sendEmptyMessage(int what) {
        synchronized (TAG) {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(what);
            } else {
                LogUtil.v(TAG, "mHandler == null");
            }
        }
    }

    public void sendEmptyMessageDelayed(int what, long delayMillis) {
        synchronized (TAG) {
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(what, delayMillis);
            } else {
                LogUtil.v(TAG, "mHandler == null");
            }
        }
    }

    public boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        synchronized (TAG) {
            if (mHandler != null) {
                return mHandler.sendEmptyMessageAtTime(what, uptimeMillis);
            } else {
                LogUtil.v(TAG, "mHandler == null");
            }
        }
        return false;
    }

    public void removeMessages(int what) {
        synchronized (TAG) {
            if (mHandler != null) {
                mHandler.removeMessages(what);
            } else {
                LogUtil.v(TAG, "mHandler == null");
            }
        }
    }

    public void removeMessages(int what, Object object) {
        synchronized (TAG) {
            if (mHandler != null) {
                mHandler.removeMessages(what, object);
            } else {
                LogUtil.v(TAG, "mHandler == null");
            }
        }
    }

    abstract protected boolean recvHandleMessage(Message msg);

    private class MsgHandlerThread extends HandlerThread implements Handler.Callback {

        public MsgHandlerThread(String name) {
            super(name);
        }

        public MsgHandlerThread(String name, int priority) {
            super(name, priority);
        }

        @Override
        public boolean handleMessage(Message msg) {
            return recvHandleMessage(msg);
        }
    }

    /**
     * 不推荐使用 by danny
     */
    public final boolean runWithScissors(final Runnable r, long timeout) {
        if (r == null) {
            throw new IllegalArgumentException("runnable must not be null");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must be non-negative");
        }

        synchronized (TAG) {
            if (mHandlerThread != null && Looper.myLooper() == mHandlerThread.getLooper()) {
                r.run();
                return true;
            }

            BlockingRunnable br = new BlockingRunnable(r);
            return br.postAndWait(mHandler, timeout);
        }
    }

    private static final class BlockingRunnable implements Runnable {
        private final Runnable mTask;
        private boolean mDone;

        public BlockingRunnable(Runnable task) {
            mTask = task;
        }

        @Override
        public void run() {
            try {
                mTask.run();
            } finally {
                synchronized (this) {
                    mDone = true;
                    notifyAll();
                }
            }
        }

        public boolean postAndWait(Handler handler, long timeout) {
            if (!handler.post(this)) {
                return false;
            }

            synchronized (this) {
                if (timeout > 0) {
                    final long expirationTime = SystemClock.uptimeMillis() + timeout;
                    while (!mDone) {
                        long delay = expirationTime - SystemClock.uptimeMillis();
                        if (delay <= 0) {
                            return false; // timeout
                        }
                        try {
                            wait(delay);
                        } catch (InterruptedException ex) {
                        }
                    }
                } else {
                    while (!mDone) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
            return true;
        }
    }


}
