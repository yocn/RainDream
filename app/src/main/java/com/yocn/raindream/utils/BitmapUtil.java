package com.yocn.raindream.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * @Author yocn
 * @Date 2019-10-18 03:26
 * @ClassName BitmapUtil
 */
public class BitmapUtil {
    public static void changeColor(Bitmap bitmap) {

    }

    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int percent) {
        long time1 = System.currentTimeMillis();
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                int color = mBitmap.getPixel(j, i);
//                LogUtil.d("" + color);
                if (color != 0) {
                    mBitmap.setPixel(j, i, Color.parseColor(DisplayUtil.getColor(percent)));
                }
            }
        }
        long time2 = System.currentTimeMillis();
        LogUtil.d("time:" + (time2 - time1));
        return mBitmap;
    }
}
