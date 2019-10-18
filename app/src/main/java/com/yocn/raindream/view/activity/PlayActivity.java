package com.yocn.raindream.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.yocn.raindream.R;
import com.yocn.raindream.base.BaseActivity;
import com.yocn.raindream.utils.BitmapUtil;
import com.yocn.raindream.utils.LogUtil;


/**
 * @Author yocn
 * @Date 2019-10-17 23:57
 * @ClassName PlayActivity
 */
public class PlayActivity extends BaseActivity implements View.OnClickListener {
    ImageView iv1;
    ImageView iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
        View rootView = getLayoutInflater().inflate(R.layout.activity_play, null);
        setContentView(rootView);
        Button b1 = rootView.findViewById(R.id.btn_b1);
        Button b2 = rootView.findViewById(R.id.btn_b2);
        iv1 = rootView.findViewById(R.id.iv1);
        iv2 = rootView.findViewById(R.id.iv2);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
    }

    int percent = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_b1:
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raindream);
                long time2 = System.currentTimeMillis();
                iv1.setImageBitmap(bitmap);
//                Bitmap bitmap1 = BitmapUtil.replaceBitmapColor(bitmap, 56);
//                long time3 = System.currentTimeMillis();
//                iv2.setImageBitmap(bitmap1);
//                LogUtil.d("   2->" + (time3 - time2));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (percent >= 90) {
                                percent = 0;
                            }
                            Bitmap bitmap1 = BitmapUtil.replaceBitmapColor(bitmap, ++percent);
                            iv1.post(new Runnable() {
                                @Override
                                public void run() {
                                    iv2.setImageBitmap(bitmap1);
                                }
                            });
                        }
                    }
                }).start();
                break;
            case R.id.btn_b2:
                break;
            default:
        }
    }
}
