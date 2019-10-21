package com.yocn.raindream.utils;

import com.yocn.raindream.R;
import com.yocn.raindream.base.BaseFragment;

import androidx.fragment.app.FragmentManager;

/**
 * @Author yocn
 * @Date 2019-10-21 14:16
 * @ClassName FragmentUtil
 */
public class FragmentUtil {
    public static void startFragment(FragmentManager manager, BaseFragment fragment, int rootView) {
        manager.beginTransaction()
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out,
                        R.anim.slide_right_in, R.anim.slide_right_out)
                .replace(rootView, fragment)
                .addToBackStack("")
                .commit();
    }
}
