package com.wlj.fixlibrary.so_fix;

import android.content.Context;
import android.os.Build;

public class SoHotFix {

    private SoFix mSoFix;

    public SoHotFix(Context context) {
        context = context.getApplicationContext();
        // 各大版本适配
        int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.O) {
            mSoFix = new SoFixImplV26(context);
        } else if (version >= Build.VERSION_CODES.M) {
            mSoFix = new SoFixImplV23(context);
        } else {
            mSoFix = new SoFixImplV20(context);
        }
    }

    public void injectLoadPath(String soDir) throws Exception {
        mSoFix.hotFix(soDir);
    }

}