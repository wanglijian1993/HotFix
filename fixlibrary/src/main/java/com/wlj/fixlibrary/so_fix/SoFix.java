package com.wlj.fixlibrary.so_fix;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Field;

public abstract class SoFix {

   protected Object mPathList;
   protected Field mNativeLibraryElementsField;

   public SoFix(Context context){
       ClassLoader classLoader=context.getClassLoader();
       mPathList=ReflectUtil.getFiledValue(classLoader,"pathList");
   }

   protected abstract void reflectNativeLibraryElements();

   public abstract Object createNativeLibraryElement(String soPath) throws Exception;

   public void hotFix(String soPath)throws Exception{
       reflectNativeLibraryElements();
       Object nativeLibraryPathElements=mNativeLibraryElementsField.get(mPathList);
       Object firstElement=createNativeLibraryElement(soPath);
       // 往 nativeLibraryPathElements 数组最前面插入一个 element
       Object newLibraryPathElements = CommonUtils.insertElementAtFirst(firstElement, nativeLibraryPathElements);
       // 重新给 pathList 中的 nativeLibraryPathElements 重新赋值
       mNativeLibraryElementsField.set(mPathList, newLibraryPathElements);
   }



}
