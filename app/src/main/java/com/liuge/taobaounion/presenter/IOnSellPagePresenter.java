package com.liuge.taobaounion.presenter;

import com.liuge.taobaounion.base.IBasePresenter;
import com.liuge.taobaounion.view.IOnSellPageCallback;

/**
 * FileName: IOnSellPagePresenter
 * Author: LiuGe
 * Date: 2020/8/26 21:22
 * Description: 特惠presenter接口
 */
public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPageCallback> {
    /**
     * 加载特惠内容
     */
    void getOnSellContent();

    /**
     * 重新加载内容
     * @Call 网络出问题时调用
     */
    void reLoad();

    /**
     * 加载更多特惠内容
     */
    void loadMore();

}
