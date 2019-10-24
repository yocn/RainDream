package com.yocn.raindream.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yocn.raindream.R;
import com.yocn.raindream.base.BaseFragment;
import com.yocn.raindream.model.JumpBean;
import com.yocn.raindream.model.audio.AudioBean;
import com.yocn.raindream.presenter.audio.MAudioManager;
import com.yocn.raindream.utils.LogUtil;

import java.util.List;

import raindream.yocn.nativelib.NativeJNI;


/**
 * @Author yocn
 * @Date 2019-10-21 14:13
 * @ClassName PlayFragment
 */
public class PlayFragment extends BaseFragment {

    JumpBean mJumpBean;
    RelativeLayout mRootRL;
    List<AudioBean> mAudioList;
    SeekBar sk1;
    SeekBar sk2;

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
        mAudioList = mJumpBean.getAudios();
        LogUtil.d("list->" + mAudioList.toString());
    }

    private void initView(View view) {
        mRootRL = view.findViewById(R.id.rl_root);
        LogUtil.d("mJumpBean.getColor()-->" + mJumpBean.getColor());
        mRootRL.setBackgroundResource(mJumpBean.getColor());
        sk1 = view.findViewById(R.id.sk_1);
        sk2 = view.findViewById(R.id.sk_2);
        sk1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MAudioManager.getInstance().setVolume(0, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sk2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MAudioManager.getInstance().setVolume(1, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initData() {
        MAudioManager.getInstance().initAPlayer("guitar.wav");
        MAudioManager.getInstance().initAPlayer("lake_rain.wav");
        MAudioManager.getInstance().start();
        MAudioManager.getInstance().setAllVolume(30);
        Object[] oo = new Object[]{"-version"};
        NativeJNI.execCmd(oo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MAudioManager.getInstance().stop();
    }

}
