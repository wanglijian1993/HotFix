package com.wlj.fixlibrary.res_fix;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;
import android.view.ContextThemeWrapper;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;

public class RexFix {
    public static void monkeyPatchExistingResources(
            Context context, String externalResourceFile, Collection<Activity> activities) {

        if (externalResourceFile == null) {
            return;
        }

        try {
            // 反射创建新的AssetManager
            AssetManager newAssetManager = AssetManager.class.getConstructor(
                    new Class[0]).newInstance(new Object[0]);
            Method mAddAssetPath = AssetManager.class.getDeclaredMethod(
                    "addAssetPath", new Class[]{String.class});
            mAddAssetPath.setAccessible(true);
            // 反射调用addAssetPath方法加载外部资源
            if (((Integer) mAddAssetPath.invoke(
                    newAssetManager, new Object[]{externalResourceFile})).intValue() == 0) {
                throw new IllegalStateException("Could not create new AssetManager");
            }
            Method mEnsureStringBlocks = AssetManager.class.getDeclaredMethod(
                    "ensureStringBlocks", new Class[0]);
            mEnsureStringBlocks.setAccessible(true);
            mEnsureStringBlocks.invoke(newAssetManager, new Object[0]);
            if (activities != null) {
                for (Activity activity : activities) {
                    Resources resources = activity.getResources();
                    try {
                        // 把Resources中的mAssets替换为newAssetManager
                        Field mAssets = Resources.class.getDeclaredField("mAssets");
                        mAssets.setAccessible(true);
                        mAssets.set(resources, newAssetManager);
                    } catch (Throwable ignore) {
                        /* ... */
                    }
                    // 获取Activity的主题
                    Resources.Theme theme = activity.getTheme();
                    try {
                        try {
                            // 把Resources.Theme中的mAssets替换为newAssetManager
                            Field ma = Resources.Theme.class.getDeclaredField("mAssets");
                            ma.setAccessible(true);
                            ma.set(theme, newAssetManager);
                        } catch (NoSuchFieldException ignore) {
                            /* ... */
                        }
                        /* ... */
                    } catch (Throwable e) {
                        /* ... */
                    }
                }
//                Collection<WeakReference<Resources>> references = null;
//                /* ...根据不同SDK版本，用不同方式得到Resources的弱引用集合 */
//                for (WeakReference<Resources> wr : references) {
//                    Resources resources = wr.get();
//                    if (resources != null) {
//                        try {
//                            // 把每个Resources中的mAssets替换为newAssetManager
//                            Field mAssets = Resources.class.getDeclaredField("mAssets");
//                            mAssets.setAccessible(true);
//                            mAssets.set(resources, newAssetManager);
//                        } catch (Throwable ignore) {
//                            /* ... */
//                        }
//                        resources.updateConfiguration(
//                                resources.getConfiguration(), resources.getDisplayMetrics());
//                    }
//                }
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }


}
