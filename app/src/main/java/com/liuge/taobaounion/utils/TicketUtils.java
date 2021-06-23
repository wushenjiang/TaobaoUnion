package com.liuge.taobaounion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.liuge.taobaounion.base.BaseApplication;
import com.liuge.taobaounion.model.domain.IBaseInfo;
import com.liuge.taobaounion.presenter.ITicketPresenter;
import com.liuge.taobaounion.ui.activity.TickerActivity;

/**
 * FileName: TicketUtils
 * Author: LiuGe
 * Date: 2020/8/27 20:41
 * Description: 淘口令工具类
 */
public class TicketUtils {

    public static void toTicketPage(Context context,IBaseInfo baseInfo){
        // 点击跳转到淘口令界面
        // 处理数据
        String title = baseInfo.getTitle();
        // 详情的地址
        String url = baseInfo.getUrl();
        if(TextUtils.isEmpty(url)){
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        // 拿到ticketPresenter去加载数据
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        context.startActivity(new Intent(context,TickerActivity.class));
    }
}
