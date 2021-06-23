package com.liuge.taobaounion.base;

/**
 * FileName: IBaseCallback
 * Author: LiuGe
 * Date: 2020/8/24 20:59
 * Description: 基类callback
 */
public interface IBaseCallback {

    void onNetworkError();

    void onLoading();

    void onEmpty();
}
