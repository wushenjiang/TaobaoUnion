package com.liuge.taobaounion.presenter.impl;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.liuge.taobaounion.model.API;
import com.liuge.taobaounion.model.domain.OnSellContent;
import com.liuge.taobaounion.presenter.IOnSellPagePresenter;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.RetrofitManager;
import com.liuge.taobaounion.view.IOnSellPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * FileName: OnSellPagePresenterImp
 * Author: LiuGe
 * Date: 2020/8/26 21:35
 * Description: 特惠presenter实现
 */
public class OnSellPagePresenterImpl implements IOnSellPagePresenter {
    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSellPageCallback mViewCallback = null;
    private final API mApi;

    public OnSellPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(API.class);
    }

    @Override
    public void getOnSellContent() {
        if(isLoading){
            return;
        }
        isLoading = true;
        // 通知UI状态为加载中
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }
        // 获取特惠内容
        Call<OnSellContent> task = mApi.getOnSellContent(mCurrentPage);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                isLoading = false;
                int code = response.code();
                LogUtils.d(OnSellPagePresenterImpl.this, "getOnSellContent result code --> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onSuccess(result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onError();
            }
        });
    }

    private void onSuccess(OnSellContent result) {
        if (mViewCallback != null) {
            try {
                if (isEmpty(result)) {
                    onEmpty();
                } else {
                    mViewCallback.onContentLoadedSuccess(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onEmpty();
            }
        }
    }

    private boolean isEmpty(OnSellContent result) {
        try {
            return result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size() == 0;
        }catch (Exception e){
            return true;
        }
    }

    private void onEmpty() {
        if (mViewCallback != null) {
            mViewCallback.onEmpty();
        }
    }

    private void onError() {
        isLoading = false;
        if (mViewCallback != null) {
            mViewCallback.onNetworkError();
        }
    }

    @Override
    public void reLoad() {
        // 重新加载
        getOnSellContent();
    }

    /**
     * 当前加载状态
     */
    private boolean isLoading = false;

    /**
     * 加载更多，通知UI
     */
    @Override
    public void loadMore() {
        if(isLoading){
            return;
        }
        isLoading = true;
        // 加载更多
        mCurrentPage++;
        // 去加载更多内容
        Call<OnSellContent> task = mApi.getOnSellContent(mCurrentPage);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                isLoading = false;
                int code = response.code();
                LogUtils.d(OnSellPagePresenterImpl.this, "getOnSellContent result code --> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    onLoadMore(result);
                } else {
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onLoadMoreError();
            }
        });

    }

    private void onLoadMoreError() {
        isLoading = false;
        mCurrentPage--;
        if (mViewCallback != null) {
//            mCurrentPage--;
            mViewCallback.onLoadMoreError();
        }
    }

    private void onLoadMore(OnSellContent result) {
        if (mViewCallback != null) {
            if (isEmpty(result)) {
                mCurrentPage--;
                mViewCallback.onLoadMoreEmpty();
            } else {
                mViewCallback.onLoadMore(result);
            }
        }
    }

    @Override
    public void registerViewCallback(IOnSellPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unRegisterViewCallback(IOnSellPageCallback callback) {
        this.mViewCallback = null;
    }
}
