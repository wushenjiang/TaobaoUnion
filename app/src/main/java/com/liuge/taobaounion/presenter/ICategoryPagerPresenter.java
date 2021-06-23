package com.liuge.taobaounion.presenter;

import com.liuge.taobaounion.base.IBasePresenter;
import com.liuge.taobaounion.view.ICategoryPagerCallback;

/**
 * FileName: ICategoryPagerPresenter
 * Author: LiuGe
 * Date: 2020/8/24 20:32
 * Description: 分类的presenter
 */
public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {
    /**
     * 根据分类id获取内容
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    void loadMore(int categoryId);

    void reload(int categoryId);

}
