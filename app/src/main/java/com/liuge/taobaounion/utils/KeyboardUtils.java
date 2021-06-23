package com.liuge.taobaounion.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * FileName: KeyboardUtils
 * Author: LiuGe
 * Date: 2020/8/29 14:56
 * Description: 隐藏键盘工具类
 */
public class KeyboardUtils {

    public static  void hide(Context context, View view){
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

    public static void show(Context context, View view){
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view,0);
    }
}
