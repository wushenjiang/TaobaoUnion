package com.liuge.taobaounion.utils;

import android.widget.Toast;

import com.liuge.taobaounion.base.BaseApplication;

/**
 * FileName: ToastUtils
 * Author: LiuGe
 * Date: 2020/8/25 21:15
 * Description: toast工具类
 */
public class ToastUtils {

    private static Toast sToast;

    public static void showToast(String tips){
        if(sToast == null){
            sToast = Toast.makeText(BaseApplication.getAppContext(),"",Toast.LENGTH_SHORT);
            sToast.setText(tips);
        }else{
            sToast.setText(tips);
        }
        sToast.show();
    }
}
