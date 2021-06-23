package com.liuge.taobaounion.presenter.impl;

import com.liuge.taobaounion.model.API;
import com.liuge.taobaounion.model.domain.HomePagerContent;
import com.liuge.taobaounion.presenter.ICategoryPagerPresenter;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.RetrofitManager;
import com.liuge.taobaounion.utils.UrlUtils;
import com.liuge.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * FileName: CategoryPagerPresenterImpl
 * Author: LiuGe
 * Date: 2020/8/24 21:13
 * Description: categoryPresenter的实现类
 */
public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    public static final int DEFAULT_PAGE = 1;
    private Integer mCurrentPage;


    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        // 根据分类id去加载内容
        // TODO:
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this, "code -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
//                    LogUtils.d(CategoryPagerPresenterImpl.this, "pageContent -->" + pagerContent);
                    // 更新UI
                    handleHomePagerContentResult(pagerContent, categoryId);
                } else {
                    handleNetWorkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this, "onFailure --" + t.toString());
                handleNetWorkError(categoryId);
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.d(this, "home pager url --> " + homePagerUrl);
        return api.getHomePageContent(homePagerUrl);
    }

    private void handleNetWorkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onNetworkError();
            }
        }
    }

    private void handleHomePagerContentResult(HomePagerContent pagerContent, int categoryId) {
        List<HomePagerContent.DataBean> data = pagerContent.getData();
        // 通知UI层更新数据
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pagerContent == null || pagerContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePagerContent.DataBean> looperData = data.subList(0, 5);
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loadMore(int categoryId) {
        // 去加载数据
        // 1.拿到当前页码
        mCurrentPage = pagesInfo.get(categoryId);
        if (mCurrentPage == null) {
            mCurrentPage = 1;
        }
        // 2.页码增加
        mCurrentPage++;
        pagesInfo.put(categoryId,mCurrentPage);
        // 3.加载数据
        Call<HomePagerContent> task = createTask(categoryId, mCurrentPage);
        // 4.处理数据结果
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                // 结果
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this, "result code -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    LogUtils.d(CategoryPagerPresenterImpl.this,"result-->" + result.toString());
                    handleLoaderMoreResult(result, categoryId);

                } else {
                    // 请求失败
                    handleLoaderMoreError(categoryId);

                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                // 失败
                LogUtils.d(CategoryPagerPresenterImpl.this, t.toString());
                handleLoaderMoreError(categoryId);
            }
        });
    }

    private void handleLoaderMoreResult(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoadMoreEmpty();
                } else {
                    callback.onLoadMoreLoaded(result.getData());
                }
            }
        }
    }

    private void handleLoaderMoreError(int categoryId) {
        mCurrentPage--;
        pagesInfo.put(categoryId, mCurrentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoadMoreError();
            }
        }

    }

    @Override
    public void reload(int categoryId) {

    }

    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }
}
