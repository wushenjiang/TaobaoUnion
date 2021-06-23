package com.liuge.taobaounion.base;

import com.liuge.taobaounion.view.IHomeCallback;

/**
 * FileName: IBasePrensenter
 * Author: LiuGe
 * Date: 2020/8/24 21:10
 * Description: 基类presenter
 */
public interface IBasePresenter<T> {

    /**
     * 注册UI接口
     * @param callback
     */
    void registerViewCallback(T callback);

    /**
     * 取消注册接口
     * @param callback
     */
    void unRegisterViewCallback(T callback);
}
