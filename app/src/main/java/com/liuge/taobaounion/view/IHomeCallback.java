package com.liuge.taobaounion.view;

import com.liuge.taobaounion.base.IBaseCallback;
import com.liuge.taobaounion.model.domain.Categories;

/**
 * FileName: IHomeCallback
 * Author: LiuGe
 * Date: 2020/8/24 0:32
 * Description: 首页回调接口
 */
public interface IHomeCallback  extends IBaseCallback {
    /**
     * 分类已加载
     */
    void onCategoriesLoaded(Categories categories);


}
