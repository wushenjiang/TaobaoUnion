package com.liuge.taobaounion.view;

import com.liuge.taobaounion.base.IBaseCallback;
import com.liuge.taobaounion.model.domain.HomePagerContent;

import java.util.List;

/**
 * FileName: ICategoryPagerCallback
 * Author: LiuGe
 * Date: 2020/8/24 20:34
 * Description: 分类的回调
 */
public interface ICategoryPagerCallback extends IBaseCallback {
    /**
     * 数据加载完毕
     *
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    int getCategoryId();


    /**
     * 加载更多 出现网络错误
     */
    void onLoadMoreError();

    /**
     * 没有更多内容
     */
    void onLoadMoreEmpty();

    /**
     * 加载到更多内容
     *
     * @param contents
     */
    void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 轮播图内容加载完毕
     *
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

}
