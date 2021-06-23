package com.liuge.taobaounion.presenter.impl;

import com.liuge.taobaounion.model.API;
import com.liuge.taobaounion.model.domain.Categories;
import com.liuge.taobaounion.presenter.IHomePresenter;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.RetrofitManager;
import com.liuge.taobaounion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * FileName: HomePresenterImpl
 * Author: LiuGe
 * Date: 2020/8/24 0:35
 * Description: 首页presenter实现类
 */
public class HomePresenterImpl implements IHomePresenter {
    private IHomeCallback mCallback;

    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        // 加载分类数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                // 数据结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this, "code -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    // 请求成功
                    Categories categories = response.body();
//                    LogUtils.d(HomePresenterImpl.this,categories.toString());
                    if (mCallback != null) {
                        if (categories == null || categories.getData().size() == 0) {
                            mCallback.onEmpty();
                        } else {
                            mCallback.onCategoriesLoaded(categories);
                        }
                    }
                } else {
                    // 请求失败
                    LogUtils.i(HomePresenterImpl.this, "请求失败....");
                    if (mCallback != null) {
                        mCallback.onNetworkError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                // 加载失败的结果
                LogUtils.e(HomePresenterImpl.this, "请求错误 -->" + t);
                if (mCallback != null) {
                    mCallback.onNetworkError();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.mCallback = callback;

    }

    @Override
    public void unRegisterViewCallback(IHomeCallback callback) {
        mCallback = null;
    }
}
