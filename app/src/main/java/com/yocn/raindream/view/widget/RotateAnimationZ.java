package com.yocn.raindream.view.widget;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @Author yocn
 * @Date 2019-10-25 10:13
 * @ClassName RotateAnimationZ
 */
public class RotateAnimationZ extends Animation {

    int centerX, centerY;
    Camera camera = new Camera();
    public final int X = 0;
    public final int Y = 1;
    public int direction = Y;
    public boolean isZhengfangxiang = true;
    private int degree = 180;

    @Override

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        //获取中心点坐标
//        centerX = width / 2;
//        centerY = height / 2;
        centerX = width;
        centerY = 0;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        final Matrix matrix = t.getMatrix();
        camera.save();
        //中心是绕Y轴旋转  这里可以自行设置X轴 Y轴 Z轴

        if (direction == X) {
            if (isZhengfangxiang) {
                camera.rotateX(degree * interpolatedTime);
            } else {
                camera.rotateX(degree - degree * interpolatedTime);
            }
        } else {
            if (isZhengfangxiang) {
                camera.rotateY(degree * interpolatedTime);
            } else {
                camera.rotateY(degree - degree * interpolatedTime);
            }
        }

        //把我们的摄像头加在变换矩阵上
        camera.getMatrix(matrix);
        camera.restore();
        //设置翻转中心点
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
