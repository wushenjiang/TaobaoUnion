package com.liuge.taobaounion.view;

import com.liuge.taobaounion.base.IBaseCallback;
import com.liuge.taobaounion.model.domain.OnSellContent;

/**
 * FileName: IOnSellPageCallback
 * Author: LiuGe
 * Date: 2020/8/26 21:23
 * Description: 特惠回调接口
 */
public interface IOnSellPageCallback extends IBaseCallback {
    /**
     * 特惠内容加载完毕
     *
     * @param content
     */
    void onContentLoadedSuccess(OnSellContent content);

    /**
     * 加载更多的结果
     *
     * @param content
     */
    void onLoadMore(OnSellContent content);

    /**
     * 加载更多失败
     */
    void onLoadMoreError();

    /**
     * 没有更多内容
     */
    void onLoadMoreEmpty();
}
