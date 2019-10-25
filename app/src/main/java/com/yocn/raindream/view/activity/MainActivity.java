package com.yocn.raindream.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.yocn.raindream.R;
import com.yocn.raindream.base.BaseActivity;
import com.yocn.raindream.model.JumpBean;
import com.yocn.raindream.presenter.audio.AudioDataManager;
import com.yocn.raindream.utils.FragmentUtil;
import com.yocn.raindream.view.adapter.MainAdapter;
import com.yocn.raindream.view.fragment.PlayFragment;
import com.yocn.raindream.view.widget.RotateAnimationZ;

import java.io.Serializable;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    FrameLayout mFragmentView;
    FragmentManager mFragmentManager;
    MainAdapter.OnItemClickInterface onItemClickInterface;
    RelativeLayout mFrontRL;

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
        mFrontRL = root.findViewById(R.id.rl_front);
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

    }

    public void rotateAnimZ(boolean open) {
        RotateAnimationZ animation = new RotateAnimationZ();
        animation.isZhengfangxiang = !open;
        animation.direction = animation.Y;
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mFrontRL.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!open) {
                    mFrontRL.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        animation.setRepeatCount(Animation.ABSOLUTE);
//        animation.setRepeatMode(Animation.REVERSE);
        mFrontRL.startAnimation(animation);
    }

}
