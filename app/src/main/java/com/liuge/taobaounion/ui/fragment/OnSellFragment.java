package com.liuge.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.base.BaseFragment;
import com.liuge.taobaounion.model.domain.IBaseInfo;
import com.liuge.taobaounion.model.domain.OnSellContent;
import com.liuge.taobaounion.presenter.IOnSellPagePresenter;
import com.liuge.taobaounion.presenter.ITicketPresenter;
import com.liuge.taobaounion.ui.activity.TickerActivity;
import com.liuge.taobaounion.ui.adapter.OnSellPageContentAdapter;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.PresenterManager;
import com.liuge.taobaounion.utils.SizeUtils;
import com.liuge.taobaounion.utils.TicketUtils;
import com.liuge.taobaounion.utils.ToastUtils;
import com.liuge.taobaounion.view.IOnSellPageCallback;

import butterknife.BindView;

/**
 * FileName: RedPacketFragment
 * Author: LiuGe
 * Date: 2020/8/23 23:37
 * Description: 特惠fragment
 */
public class OnSellFragment extends BaseFragment implements IOnSellPageCallback, OnSellPageContentAdapter.OnSellPageItemClickListener {

    public static final int DEFAULT_SPAN_COUNT = 2;

    @BindView(R.id.on_sell_content_list)
    public RecyclerView mContentList;
    @BindView(R.id.on_sell_refresh_layout)
    public TwinklingRefreshLayout mRefreshLayout;
    @BindView(R.id.fragment_bar_title)
    public TextView barTitleTv;

    private IOnSellPagePresenter mOnSellPagePresenter;
    private OnSellPageContentAdapter mOnSellPageContentAdapter;

    @Override
    protected void initPresenter() {
        mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();
    }

    @Override
    protected void release() {
        super.release();
        mOnSellPagePresenter.unRegisterViewCallback(this);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                // 去加载更多的内容
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loadMore();
                }
            }
        });
        mOnSellPageContentAdapter.setOnSellPageItemClickListener(this);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        mOnSellPageContentAdapter = new OnSellPageContentAdapter();
        // 设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT);
        mContentList.setLayoutManager(gridLayoutManager);
        // 设置适配器
        mContentList.setAdapter(mOnSellPageContentAdapter);
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2.5F);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2.5F);
                outRect.left = SizeUtils.dip2px(getContext(), 2.5F);
                outRect.right = SizeUtils.dip2px(getContext(), 2.5F);

            }
        });
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableOverScroll(true);
        barTitleTv.setText(R.string.text_on_sell_title);
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent content) {
        setUpState(State.SUCCESS);
        // 数据回来了
        //更新UI
        LogUtils.d(this, content.getData().toString());
        mOnSellPageContentAdapter.setData(content);
    }

    @Override
    public void onLoadMore(OnSellContent content) {
        mRefreshLayout.finishLoadmore();
        // 添加数据到列表的后面
        mOnSellPageContentAdapter.onLoadMore(content);
        // 输出个toast提示用户
        int size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtils.showToast("加载了" + size + "条数据");
    }

    @Override
    public void onLoadMoreError() {
        mRefreshLayout.finishLoadmore();
        ToastUtils.showToast("网络异常，请稍后重试~");
    }

    @Override
    public void onLoadMoreEmpty() {
        mRefreshLayout.finishLoadmore();
        ToastUtils.showToast("没有更多内容~");
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onSellItemClick(IBaseInfo data) {

        TicketUtils.toTicketPage(getContext(), data);
    }
}
