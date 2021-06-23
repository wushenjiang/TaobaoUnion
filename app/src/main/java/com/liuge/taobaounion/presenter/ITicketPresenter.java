package com.liuge.taobaounion.presenter;

import com.liuge.taobaounion.base.IBasePresenter;
import com.liuge.taobaounion.view.ITicketPagerCallback;

/**
 * FileName: ITicketPresenter
 * Author: LiuGe
 * Date: 2020/8/26 15:34
 * Description: 淘口令接口
 */
public interface ITicketPresenter extends IBasePresenter<ITicketPagerCallback> {
    /**
     * 获取优惠券，生成淘口令
     * @param title
     * @param url
     * @param cover
     */
    void getTicket(String title,String url,String cover);
}
