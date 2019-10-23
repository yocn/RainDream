package com.yocn.raindream.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yocn.raindream.R;
import com.yocn.raindream.base.BaseFragment;
import com.yocn.raindream.model.JumpBean;
import com.yocn.raindream.presenter.audio.MAudioManager;
import com.yocn.raindream.utils.LogUtil;

import raindream.yocn.nativelib.NativeJNI;


/**
 * @Author yocn
 * @Date 2019-10-21 14:13
 * @ClassName PlayFragment
 */
public class PlayFragment extends BaseFragment {

    JumpBean mJumpBean;
    RelativeLayout mRootRL;
    TextView mShowTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_play, container, false);
        preInitData();
        initView(root);
        initData();
        return root;
    }

    private void preInitData() {
        Bundle bundle = getArguments();
        mJumpBean = (JumpBean) bundle.getSerializable("jump");
    }

    private void initView(View view) {
        mShowTV = view.findViewById(R.id.tv_show);
        mRootRL = view.findViewById(R.id.rl_root);
        LogUtil.d("mJumpBean.getColor()-->" + mJumpBean.getColor());
        mRootRL.setBackgroundResource(mJumpBean.getColor());
        mShowTV.setText(mJumpBean.getShow());
    }

    private void initData() {
        MAudioManager.getInstance().initAPlayer("guitar.wav");
        MAudioManager.getInstance().initAPlayer("lake_rain.wav");
        MAudioManager.getInstance().start();
        Object[] oo = new Object[]{"-version"};
        NativeJNI.execCmd(oo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MAudioManager.getInstance().stop();
    }
}
