package com.liuge.taobaounion.presenter.impl;

import com.liuge.taobaounion.model.API;
import com.liuge.taobaounion.model.domain.Histories;
import com.liuge.taobaounion.model.domain.SearchRecommend;
import com.liuge.taobaounion.model.domain.SearchResult;
import com.liuge.taobaounion.presenter.ISearchPresenter;
import com.liuge.taobaounion.utils.JsonCacheUtils;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.RetrofitManager;
import com.liuge.taobaounion.view.ISearchPageCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * FileName: SearchPresenterImpl
 * Author: LiuGe
 * Date: 2020/8/27 21:01
 * Description: 搜索presenter实现
 */
public class SearchPresenterImpl implements ISearchPresenter {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_HISTORIES_SIZE = 10;
    private final API mApi;
    private ISearchPageCallback mSearchViewCallback = null;

    private int mCurrentPage = DEFAULT_PAGE;
    private String mCurrentKeyword = null;
    private JsonCacheUtils mJsonCacheUtils;

    public SearchPresenterImpl() {
        mJsonCacheUtils = JsonCacheUtils.getInstance();
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(API.class);
    }

    @Override
    public void getHistories() {
        Histories histories = mJsonCacheUtils.getValue(KEY_HISTORIES, Histories.class);
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onHistoryLoaded(histories);
        }
    }

    @Override
    public void delHistories() {
        mJsonCacheUtils.delCache(KEY_HISTORIES);
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onHistoryDeleted();
        }
    }

    public static final String KEY_HISTORIES = "key_histories";
    public int historiesMaxSize = DEFAULT_HISTORIES_SIZE;

    /**
     * 添加历史记录
     *
     * @param history
     */
    private void saveHistory(String history) {
        Histories histories = mJsonCacheUtils.getValue(KEY_HISTORIES, Histories.class);
        // 如果已经存在了，就删除再添加
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        // 去重完成
        // 处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        // 对个数进行限制
        if (historiesList.size() > historiesMaxSize) {
            historiesList = historiesList.subList(0, historiesMaxSize);
        }
        // 添加记录
        historiesList.add(history);
        histories.setHistories(historiesList);
        // 保存记录
        mJsonCacheUtils.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        if (mCurrentKeyword == null || !mCurrentKeyword.equals(keyword)) {
            this.saveHistory(keyword);
            this.mCurrentKeyword = keyword;
        }
        // 更新UI状态
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(this, "doSearch result code -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                LogUtils.d(this,"search result error => " + t.toString());
                onError();
            }
        });
    }

    private void onError() {
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onNetworkError();
        }

    }

    private void handleSearchResult(SearchResult body) {
        if (mSearchViewCallback != null) {
            if (isEmpty(body)) {
                // 数据为空
                mSearchViewCallback.onEmpty();
            } else {
                mSearchViewCallback.onSearchSuccess(body);
            }
        }
    }

    private boolean isEmpty(SearchResult body) {
        try {
            return body == null || body.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void research() {
        if (mCurrentKeyword == null) {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        } else {
            // 可以重新搜索
            this.doSearch(mCurrentKeyword);
        }
    }

    @Override
    public void loadMore() {
        mCurrentPage++;
        // 进行搜索
        if (mCurrentKeyword == null) {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        } else {
            // 去搜索
            doSearchMore();
        }
    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(this, "doSearch result code -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchMoreResult(response.body());
                } else {
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onLoadMoreError();
            }
        });
    }

    /**
     * 处理加载更多的结果
     *
     * @param body
     */
    private void handleSearchMoreResult(SearchResult body) {
        if (mSearchViewCallback != null) {
            if (isEmpty(body)) {
                // 数据为空
                mSearchViewCallback.onMoreLoadedEmpty();
            } else {
                mSearchViewCallback.onMoreLoaded(body);
            }
        }
    }

    /**
     * 加载更多失败
     */
    private void onLoadMoreError() {
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onMoreLoadedError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(SearchPresenterImpl.this, "getRecommendWords result code -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    // 处理结果
                    if (mSearchViewCallback != null) {
                        mSearchViewCallback.onRecommendWordsLoaded(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchPageCallback callback) {
        this.mSearchViewCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(ISearchPageCallback callback) {
        this.mSearchViewCallback = null;
    }
}
