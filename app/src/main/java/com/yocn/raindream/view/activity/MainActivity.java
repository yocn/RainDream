package com.yocn.raindream.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.yocn.raindream.R;
import com.yocn.raindream.base.BaseActivity;
import com.yocn.raindream.model.JumpBean;
import com.yocn.raindream.utils.BitmapUtil;
import com.yocn.raindream.utils.DisplayUtil;
import com.yocn.raindream.view.adapter.MainAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    LinearLayout mTopRL;
    ImageView mIconImageView;
    View mOtherView;

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
        View rootView = getLayoutInflater().inflate(R.layout.activity_main, null);
        setContentView(rootView);
        initView(rootView);
        initData();
    }


    private void initView(View root) {
        mRecyclerView = root.findViewById(R.id.rv_main);
        mTopRL = root.findViewById(R.id.rl_top);
        mIconImageView = root.findViewById(R.id.iv_icon);
        mOtherView = root.findViewById(R.id.iv_other);
        mTopRL.post(() -> {
            int height = getWindow().getDecorView().getMeasuredHeight();
            int width = getWindow().getDecorView().getMeasuredWidth();
        });
    }

    private int currentY;
    Bitmap bitmap;

    private void initData() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raindream);

        List<JumpBean> data = MainAdapter.getDataList();
        MainAdapter mMainAdapter = new MainAdapter(data);
        mMainAdapter.setmContext(this);
        int spanCount;
        if (data.size() < 6) {
            spanCount = 2;
        } else if (data.size() < 24) {
            spanCount = 3;
        } else {
            spanCount = 4;
        }
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                                @Override
                                                public int getSpanSize(int position) {
                                                    return position == 0 ? gridLayoutManager.getSpanCount() : 1;
                                                }
                                            }
        );

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMainAdapter);

        final int min = DisplayUtil.dip2px(this, 100);
        final int max = DisplayUtil.dip2px(this, 140);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentY += dy;
                if (currentY < min) {
//                    mTopRL.setVisibility(View.GONE);
//                    DisplayUtil.setAndroidNativeLightStatusBar(MainActivity.this, false);
                } else {
//                    mTopRL.setVisibility(View.VISIBLE);
//                    DisplayUtil.setAndroidNativeLightStatusBar(MainActivity.this, true);
                    if (currentY < max) {
                        int percent = (currentY - min) * 100 / (max - min);
//                        mTopRL.setBackgroundColor(Color.parseColor(color));
                    } else {
//                        mTopRL.setBackgroundResource(R.color.gray_deep);
                    }

                }
            }
        });
    }


}
