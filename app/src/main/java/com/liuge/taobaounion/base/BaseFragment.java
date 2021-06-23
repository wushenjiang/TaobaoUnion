package com.liuge.taobaounion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.liuge.taobaounion.R;
import com.liuge.taobaounion.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * FileName: BaseFragment
 * Author: LiuGe
 * Date: 2020/8/23 23:33
 * Description: 基类fragment
 */
public abstract class BaseFragment extends Fragment {

    private State mCurrentState = State.NONE;
    private View mLoadingView;
    private View mSuccessView;
    private View mErrorView;
    private View mEmptyView;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY;
    }

    @OnClick(R.id.network_error_tips)
    public void retry(){
        // 点击了重新加载内容
        LogUtils.d(this,"on retry...");
        onRetryClick();
    }

    /**
     * 如果子fragment需要知道网络错误以后的点击，那么覆盖方法即可
     */
    protected void onRetryClick() {

    }

    private Unbinder mBind;
    private FrameLayout mBaseContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater,container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStatusView(inflater, container);
        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();
        loadData();
        return rootView;
    }

    /**
     * 如果子类需要去设置相关的事件，可以复写此方法
     */
    protected void initListener() {

    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 加载各种状态的View
     *
     * @param inflater
     * @param container
     */
    private void loadStatusView(LayoutInflater inflater, ViewGroup container) {
        // 成功的view
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);
        // loading的view
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);
        // 错误页面
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);
        // 内容为空的页面
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);
        setUpState(State.NONE);

    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    /**
     * 子类通过这个方法来切换状态页面即可
     * @param currentState
     */
    public void setUpState(State currentState) {

        this.mCurrentState = currentState;
        mSuccessView.setVisibility(mCurrentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(mCurrentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(mCurrentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(mCurrentState == State.EMPTY?View.VISIBLE:View.GONE);
    }

    /**
     * 加载loading界面
     *
     * @param inflater
     * @param container
     * @return
     */
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected void initView(View rootView) {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 释放资源
        if (mBind != null) {
            mBind.unbind();
        }
        release();
    }

    protected void release() {
        super.onDestroyView();
    }

    protected void initPresenter() {
        // 创建presenter
    }

    protected void loadData() {
        // 加载数据

    }

    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        int resId = getRootViewResId();
        return inflater.inflate(resId, container, false);
    }

    protected abstract int getRootViewResId();
}
