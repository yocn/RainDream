package com.yocn.raindream.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yocn.raindream.R;
import com.yocn.raindream.base.BaseActivity;
import com.yocn.raindream.model.JumpBean;
import com.yocn.raindream.presenter.audio.AudioDataManager;
import com.yocn.raindream.utils.DisplayUtil;
import com.yocn.raindream.utils.FragmentUtil;
import com.yocn.raindream.view.adapter.MainAdapter;
import com.yocn.raindream.view.fragment.PlayFragment;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    LinearLayout mTopRL;
    ImageView mIconImageView;
    FrameLayout mFragmentView;
    View mOtherView;
    private int currentY;
    Bitmap bitmap;
    FragmentManager mFragmentManager;
    MainAdapter.OnItemClickInterface onItemClickInterface;


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
        mFragmentView = root.findViewById(R.id.fl_root);
        mTopRL = root.findViewById(R.id.rl_top);
        mIconImageView = root.findViewById(R.id.iv_icon);
        mOtherView = root.findViewById(R.id.iv_other);
        mTopRL.post(() -> {
            int height = getWindow().getDecorView().getMeasuredHeight();
            int width = getWindow().getDecorView().getMeasuredWidth();
        });
        onItemClickInterface = new MainAdapter.OnItemClickInterface() {
            @Override
            public void OnItemClick(JumpBean bean) {
                mFragmentView.setVisibility(View.VISIBLE);
                PlayFragment fragment = new PlayFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("jump", (Serializable) bean);
                fragment.setArguments(bundle);
                FragmentUtil.startFragment(mFragmentManager, fragment, R.id.fl_root);
            }
        };
    }


    private void initData() {
        mFragmentManager = getSupportFragmentManager();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.raindream);

        List<JumpBean> data = AudioDataManager.getDataList();
        MainAdapter mMainAdapter = new MainAdapter(data);
        mMainAdapter.setOnItemClickInterface(onItemClickInterface);
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
                    mTopRL.setVisibility(View.GONE);
//                    DisplayUtil.setAndroidNativeLightStatusBar(MainActivity.this, false);
                } else {
                    mTopRL.setVisibility(View.VISIBLE);
//                    DisplayUtil.setAndroidNativeLightStatusBar(MainActivity.this, true);
                    if (currentY < max) {
                        float percent = (currentY - min) * 1.0f / (max - min);
                        mTopRL.setAlpha(percent);
                    } else {
//                        mTopRL.setBackgroundResource(R.color.gray_deep);
                        mTopRL.setAlpha(1.0f);
                    }

                }
            }
        });
    }


}
