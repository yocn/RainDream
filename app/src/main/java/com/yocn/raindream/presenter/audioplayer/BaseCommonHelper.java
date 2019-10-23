package com.yocn.raindream.presenter.audioplayer;

public class BaseCommonHelper {
    protected BaseCommonHelper() {

    }

    protected boolean CheckStringIsNULL(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }
}
