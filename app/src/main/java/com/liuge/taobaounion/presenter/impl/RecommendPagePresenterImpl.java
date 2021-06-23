package com.liuge.taobaounion.presenter.impl;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.liuge.taobaounion.model.API;
import com.liuge.taobaounion.model.domain.Categories;
import com.liuge.taobaounion.model.domain.RecommendContent;
import com.liuge.taobaounion.model.domain.RecommendPageCategory;
import com.liuge.taobaounion.presenter.IRecommendPagePresenter;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.RetrofitManager;
import com.liuge.taobaounion.view.IRecommendPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * FileName: RecommendPagePresenterImpl
 * Author: LiuGe
 * Date: 2020/8/26 19:06
 * Description: 特惠presenter实现类
 */
public class RecommendPagePresenterImpl implements IRecommendPagePresenter {

    private final API mApi;

    public RecommendPagePresenterImpl() {
        // 拿到Retrofit
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(API.class);
    }

    private IRecommendPageCallback mViewCallback = null;

    @Override
    public void getCategories() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }

        Call<RecommendPageCategory> task = mApi.getRecommendPageCategories();
        task.enqueue(new Callback<RecommendPageCategory>() {
            @Override
            public void onResponse(Call<RecommendPageCategory> call, Response<RecommendPageCategory> response) {
                int code = response.code();
                LogUtils.d(RecommendPagePresenterImpl.this, "result code -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    RecommendPageCategory result = response.body();
                    // 通知UI更新
                    if (mViewCallback != null) {
                        mViewCallback.onCategoriesLoaded(result);
                    }
                } else {
                    // 错误
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<RecommendPageCategory> call, Throwable t) {
                // 错误
                onLoadedError();
            }
        });
    }

    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onNetworkError();
        }
    }

    @Override
    public void getContentByCategoryId(RecommendPageCategory.DataBean item) {

//        this.mCurrentCategoryItem = item;
        int categoryId = item.getFavorites_id();
        LogUtils.d(this,"categoryId -->" + categoryId);
        Call<RecommendContent> task = mApi.getRecommendPageContent(categoryId);
        task.enqueue(new Callback<RecommendContent>() {
            @Override
            public void onResponse(Call<RecommendContent> call, Response<RecommendContent> response) {
                int code = response.code();
                LogUtils.d(RecommendPagePresenterImpl.this, "getContentByCategoryId result code -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    RecommendContent result = response.body();
                    // 通知UI更新
                    if (mViewCallback != null) {
                        mViewCallback.onContentLoaded(result);
                    }
                } else {
                    // 错误
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<RecommendContent> call, Throwable t) {
                // 错误
                onLoadedError();
            }
        });

    }

    @Override
    public void reloadContent() {
        this.getCategories();
    }

    @Override
    public void registerViewCallback(IRecommendPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(IRecommendPageCallback callback) {
        this.mViewCallback = null;
    }
}
