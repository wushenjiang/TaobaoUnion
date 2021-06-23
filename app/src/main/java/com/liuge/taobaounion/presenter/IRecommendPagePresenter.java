package com.liuge.taobaounion.presenter;

import com.liuge.taobaounion.base.IBasePresenter;
import com.liuge.taobaounion.model.domain.RecommendPageCategory;
import com.liuge.taobaounion.view.IRecommendPageCallback;

/**
 * FileName: IRecommendPagePresentter
 * Author: LiuGe
 * Date: 2020/8/26 18:58
 * Description: 特惠presenter接口
 */
public interface IRecommendPagePresenter extends IBasePresenter<IRecommendPageCallback> {
    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取分类内容
     * @param item
     */
    void getContentByCategoryId(RecommendPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reloadContent();

}
