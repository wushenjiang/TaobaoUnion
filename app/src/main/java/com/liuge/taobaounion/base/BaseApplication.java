package com.liuge.taobaounion.base;

import android.app.Application;
import android.content.Context;

/**
 * FileName: BaseApplication
 * Author: LiuGe
 * Date: 2020/8/25 21:15
 * Description: 主application
 */
public class BaseApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();
    }

    public static Context getAppContext(){
        return appContext;
    }
}
