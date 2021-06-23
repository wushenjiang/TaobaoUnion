package com.liuge.taobaounion.view;

import com.liuge.taobaounion.base.IBaseCallback;
import com.liuge.taobaounion.model.domain.Histories;
import com.liuge.taobaounion.model.domain.SearchRecommend;
import com.liuge.taobaounion.model.domain.SearchResult;

import java.util.List;

/**
 * FileName: ISearchViewCallback
 * Author: LiuGe
 * Date: 2020/8/27 20:54
 * Description: 搜索的回调接口
 */
public interface ISearchPageCallback extends IBaseCallback {
    /**
     * 搜索历史结果
     * @param histories
     */
    void onHistoryLoaded(Histories histories);

    /**
     * 历史记录被删除了
     */
    void onHistoryDeleted();

    /**
     * 搜索结果:成功
     * @param result
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载到了更多内容
     * @param result
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多网络出错
     */
    void onMoreLoadedError();

    /**
     * 没有更多内容
     */
    void onMoreLoadedEmpty();

    /**
     * 推荐词获取结果
     * @param recommendWords
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);
}
