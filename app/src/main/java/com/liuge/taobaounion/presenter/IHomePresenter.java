package com.liuge.taobaounion.presenter;

import com.liuge.taobaounion.base.IBaseCallback;
import com.liuge.taobaounion.base.IBasePresenter;
import com.liuge.taobaounion.view.IHomeCallback;

/**
 * FileName: IHomePresenter
 * Author: LiuGe
 * Date: 2020/8/24 0:32
 * Description: 首页presenter接口
 */

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {

    /**
     * 获取商品分类
     */
    void getCategories();
}
