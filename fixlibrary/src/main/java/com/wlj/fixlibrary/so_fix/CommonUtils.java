package com.wlj.fixlibrary.so_fix;

import java.lang.reflect.Array;
import java.util.Objects;

public class CommonUtils {

    public static boolean equals(Object a,Object b){
        return Objects.equals(a, b);
    }

    public static Object insertElementAtFirst(Object firstObj, Object array) {
        Class<?> localClass = array.getClass().getComponentType();
        int len = Array.getLength(array) + 1;
        Object result = Array.newInstance(localClass, len);
        Array.set(result, 0, firstObj);
        for (int k = 1; k < len; ++k) {
            Array.set(result, k, Array.get(array, k - 1));
        }
        return result;
    }

}
