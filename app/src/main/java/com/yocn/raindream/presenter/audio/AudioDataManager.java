package com.yocn.raindream.presenter.audio;

import com.yocn.raindream.R;
import com.yocn.raindream.model.JumpBean;
import com.yocn.raindream.model.audio.AudioBean;
import com.yocn.raindream.utils.FileUtils;
import com.yocn.raindream.utils.StorageUtil;
import com.yocn.raindream.view.fragment.PlayFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yocn
 * @Date 2019-10-24 20:35
 * @ClassName AudioDataManager
 */
public class AudioDataManager {
    private static List<AudioBean> mAudioList = new ArrayList<>();

    public static List<JumpBean> getDataList() {
        List<JumpBean> list = new ArrayList<>();

        List<AudioBean> audioBeans = new ArrayList<>();
        audioBeans.add(mAudioList.get(8));
        audioBeans.add(mAudioList.get(10));

        list.add(new JumpBean("场景1", PlayFragment.class, R.color.color1, R.color.write, audioBeans));
        list.add(new JumpBean("场景2", PlayFragment.class, R.color.color2, R.color.black, audioBeans));
        list.add(new JumpBean("场景3", PlayFragment.class, R.color.color3, R.color.write, audioBeans));
        list.add(new JumpBean("场景4", PlayFragment.class, R.color.color4, R.color.black, audioBeans));
        list.add(new JumpBean("场景5", PlayFragment.class, R.color.color5, R.color.black, audioBeans));
        list.add(new JumpBean("场景6", PlayFragment.class, R.color.color6, R.color.black, audioBeans));
        list.add(new JumpBean("场景7", PlayFragment.class, R.color.color7, R.color.write, audioBeans));
        list.add(new JumpBean("场景8", PlayFragment.class, R.color.color8, R.color.black, audioBeans));
        list.add(new JumpBean("场景9", PlayFragment.class, R.color.color9, R.color.write, audioBeans));
        return list;
    }

    public static void initAudioList() {
        String rootPath = StorageUtil.getOggPath();
        File[] files = FileUtils.listFiles(rootPath);
        if (files == null) {
            return;
        }

        for (int i = 0; i < files.length; ++i) {
            AudioBean audioBean = new AudioBean();
            audioBean.setName(files[i].getName());
            audioBean.setPath(files[i].getAbsolutePath());
            audioBean.setId(i);
            mAudioList.add(audioBean);
        }
    }
}
