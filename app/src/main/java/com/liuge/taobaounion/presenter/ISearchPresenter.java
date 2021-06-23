package com.liuge.taobaounion.presenter;

import com.liuge.taobaounion.base.IBasePresenter;
import com.liuge.taobaounion.view.ISearchPageCallback;

/**
 * FileName: ISearchPresenter
 * Author: LiuGe
 * Date: 2020/8/27 20:53
 * Description: 搜索的接口
 */
public interface ISearchPresenter extends IBasePresenter<ISearchPageCallback> {

    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 清空搜索历史
     */
    void delHistories();

    /**
     * 搜索
     * @param keyword
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void research();

    /**
     * 加载更多
     */
    void loadMore();

    /**
     * 获取推荐词
     */
    void getRecommendWords();
}
