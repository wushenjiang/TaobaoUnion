package com.liuge.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.base.BaseFragment;
import com.liuge.taobaounion.model.domain.IBaseInfo;
import com.liuge.taobaounion.model.domain.RecommendContent;
import com.liuge.taobaounion.model.domain.RecommendPageCategory;
import com.liuge.taobaounion.presenter.IRecommendPagePresenter;
import com.liuge.taobaounion.presenter.ITicketPresenter;
import com.liuge.taobaounion.ui.activity.TickerActivity;
import com.liuge.taobaounion.ui.adapter.RecommendPageContentAdapter;
import com.liuge.taobaounion.ui.adapter.RecommendPageLeftAdapter;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.PresenterManager;
import com.liuge.taobaounion.utils.SizeUtils;
import com.liuge.taobaounion.utils.TicketUtils;
import com.liuge.taobaounion.view.IRecommendPageCallback;

import java.util.List;

import butterknife.BindView;

/**
 * FileName: HomeFragment
 * Author: LiuGe
 * Date: 2020/8/23 23:29
 * Description: 推荐的fragment
 */
public class RecommendFragment extends BaseFragment implements IRecommendPageCallback, RecommendPageLeftAdapter.OnLeftItemClickListener, RecommendPageContentAdapter.OnRecommendPageContentItemClickListener {

    private IRecommendPagePresenter mRecommendPagePresenter;

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;

    @BindView(R.id.fragment_bar_title)
    public TextView barTitleTv;
    private RecommendPageLeftAdapter mLeftAdapter;
    private RecommendPageContentAdapter mRightAdapter;

    @Override
    protected void initPresenter() {
        mRecommendPagePresenter = PresenterManager.getInstance().getRecommendPagePresenter();
        mRecommendPagePresenter.registerViewCallback(this);
        mRecommendPagePresenter.getCategories();
    }

    @Override
    protected void release() {
        super.release();
        if (mRecommendPagePresenter != null) {
            mRecommendPagePresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_recommend;
    }
    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_with_bar_layout,container,false);
    }
    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new RecommendPageLeftAdapter();
        leftCategoryList.setAdapter(mLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new RecommendPageContentAdapter();
        rightContentList.setAdapter(mRightAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dip2px(getContext(), 4);
                int leftAndRight = SizeUtils.dip2px(getContext(), 6);
                outRect.top = topAndBottom;
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
                outRect.bottom = topAndBottom;
            }
        });
        barTitleTv.setText(R.string.text_recommend_title);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setOnLeftItemClickListener(this);
        mRightAdapter.setOnRecommendPageContentItemClickListener(this);
    }

    @Override
    protected void onRetryClick() {
        // 重试
        if (mRecommendPagePresenter != null) {
            mRecommendPagePresenter.reloadContent();
        }
    }

    @Override
    public void onCategoriesLoaded(RecommendPageCategory categories) {
        setUpState(State.SUCCESS);
        mLeftAdapter.setData(categories);
        // 分类内容
        LogUtils.d(this, "onCategoriesLoaded -->" + categories);
        // TODO:更新UI
        // 根据当前选中的分类，获取分类详情内容
        List<RecommendPageCategory.DataBean> data = categories.getData();
        mRecommendPagePresenter.getContentByCategoryId(data.get(0));
    }

    @Override
    public void onContentLoaded(RecommendContent content) {
        LogUtils.d(this,"content => " + content);
        mRightAdapter.setData(content);
        rightContentList.scrollToPosition(0);
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

    }


    @Override
    public void onLeftItemClick(RecommendPageCategory.DataBean item) {
        // 左边的分类被点击了
        mRecommendPagePresenter.getContentByCategoryId(item);
        LogUtils.d(this, "current selected item -->" + item.getFavorites_title());
    }

    @Override
    public void onContentItemClick(IBaseInfo item, boolean hasOffSale) {
        // 如果有优惠卷才能点击跳转
        LogUtils.d(this,"hasOffSale -->" + hasOffSale);
        if (hasOffSale) {
            TicketUtils.toTicketPage(getContext(),item);
        }
    }
}
