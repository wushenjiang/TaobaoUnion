package com.liuge.taobaounion.view;

import com.liuge.taobaounion.base.IBaseCallback;
import com.liuge.taobaounion.model.domain.TicketResult;

/**
 * FileName: ITickctPagerCallback
 * Author: LiuGe
 * Date: 2020/8/26 15:34
 * Description: 淘口令接口回调
 */
public interface ITicketPagerCallback extends IBaseCallback {

    /**
     * 淘口加载结果回调
     * @param cover
     * @param result
     */
    void onTicketLoaded(String cover, TicketResult result);
}
