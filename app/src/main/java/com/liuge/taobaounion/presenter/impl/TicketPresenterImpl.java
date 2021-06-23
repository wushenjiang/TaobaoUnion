package com.liuge.taobaounion.presenter.impl;

import com.liuge.taobaounion.model.API;
import com.liuge.taobaounion.model.domain.TicketParams;
import com.liuge.taobaounion.model.domain.TicketResult;
import com.liuge.taobaounion.presenter.ITicketPresenter;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.RetrofitManager;
import com.liuge.taobaounion.utils.UrlUtils;
import com.liuge.taobaounion.view.ITicketPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * FileName: TicketPresenterImpl
 * Author: LiuGe
 * Date: 2020/8/26 15:38
 * Description: 淘口令presenter接口实现
 */
public class TicketPresenterImpl implements ITicketPresenter {
    private ITicketPagerCallback mViewCallback = null;
    private String mCover = null;
    private TicketResult mTicketResult;

    private enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentLoadState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        this.onTicketLoading();
        this.mCover = cover;
        LogUtils.d(this, "title -->" + title);
        LogUtils.d(this, "url -->" + url);
        String targetUrl = UrlUtils.getTicketUrl(url);
        // 去获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        TicketParams ticketParams = new TicketParams(targetUrl, title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                LogUtils.d(TicketPresenterImpl.this, "code -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    // 请求成功
                    mTicketResult = response.body();
//                    LogUtils.d(TicketPresenterImpl.this,"result -->" +ticketResult);
                    // 通知UI更新
                    onTicketLoadedSuccess();
                } else {
                    // 请求失败
                    onLoadedTicketError();

                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                // 失败
                onLoadedTicketError();
            }
        });
    }

    private void onTicketLoadedSuccess() {
        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover, mTicketResult);
        }else{
            mCurrentLoadState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallback != null) {
            mViewCallback.onNetworkError();
        }else{
            mCurrentLoadState = LoadState.ERROR;
        }

    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        this.mViewCallback = callback;
        if (mCurrentLoadState != LoadState.NONE) {
            // 说明状态已经改变了
            // 更新UI
            if (mCurrentLoadState == LoadState.SUCCESS) {
                onTicketLoadedSuccess();

            } else if (mCurrentLoadState == LoadState.ERROR) {
                onLoadedTicketError();
            }else if(mCurrentLoadState == LoadState.LOADING){
                onTicketLoading();
            }
        }
    }

    private void onTicketLoading() {
        if (mViewCallback != null) {
            mViewCallback.onLoading();
        }else{
            mCurrentLoadState = LoadState.LOADING;
        }
    }

    @Override
    public void unRegisterViewCallback(ITicketPagerCallback callback) {
        this.mViewCallback = null;
    }
}
