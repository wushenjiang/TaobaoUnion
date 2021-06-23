package com.liuge.taobaounion.view;

import com.liuge.taobaounion.base.IBaseCallback;
import com.liuge.taobaounion.model.domain.RecommendContent;
import com.liuge.taobaounion.model.domain.RecommendPageCategory;

/**
 * FileName: IRecommendPageCallback
 * Author: LiuGe
 * Date: 2020/8/26 18:59
 * Description: 特惠回调接口
 */
public interface IRecommendPageCallback extends IBaseCallback {
    /**
     * 分类内容结果
     * @param categories 分类内容
     */
    void onCategoriesLoaded(RecommendPageCategory categories);

    /**
     * 内容结果
     * @param content
     */
    void onContentLoaded(RecommendContent content);
}
