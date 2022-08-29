package com.wlj.fixlibrary.so_fix;

import android.content.Context;

import java.io.File;
/**
 * 6.0 以下的版本
 */
public class SoFixImplV20 extends SoFix {
    public SoFixImplV20(Context context) {
        super(context);
    }

    @Override
    protected void reflectNativeLibraryElements() {
        mNativeLibraryElementsField = ReflectUtil.getFiled(mPathList, "nativeLibraryDirectories");
    }

    @Override
    public Object createNativeLibraryElement(String soPath) throws Exception {
        return new File(soPath);
    }
}