package raindream.yocn.nativelib;

/**
 * @Author yocn
 * @Date 2019-10-22 20:14
 * @ClassName NativeJNI
 */
public class NativeJNI {

    static {
        System.loadLibrary("av_ffmpeg");
    }

    public static native void helloFFmpeg();

    public static native int execCmd(Object[] args);

}
