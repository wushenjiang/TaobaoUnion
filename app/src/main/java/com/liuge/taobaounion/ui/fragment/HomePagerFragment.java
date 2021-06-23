package com.liuge.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.base.BaseFragment;
import com.liuge.taobaounion.model.domain.Categories;
import com.liuge.taobaounion.model.domain.HomePagerContent;
import com.liuge.taobaounion.model.domain.IBaseInfo;
import com.liuge.taobaounion.presenter.ICategoryPagerPresenter;
import com.liuge.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.liuge.taobaounion.ui.adapter.LooperPagerAdapter;
import com.liuge.taobaounion.ui.custom.AutoLoopViewPager;
import com.liuge.taobaounion.utils.Constants;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.PresenterManager;
import com.liuge.taobaounion.utils.SizeUtils;
import com.liuge.taobaounion.utils.TicketUtils;
import com.liuge.taobaounion.utils.ToastUtils;
import com.liuge.taobaounion.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;


/**
 * FileName: HomePagerFragment
 * Author: LiuGe
 * Date: 2020/8/24 1:19
 * Description: 首页下的fragment
 */
public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, LinearItemContentAdapter.onListItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {

    private ICategoryPagerPresenter mCategoryPagerPresenter;
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitle;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_nest_scroller)
    public TbNestedScrollView homePagerNestedView;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout mRefreshLayout;
    @BindView(R.id.home_pager_parent)
    public LinearLayout mHomePagerParent;


    private LinearItemContentAdapter mContentAdapter;

    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        //
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 可见的时候我们调用,开始轮播
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 不可见的时候停止轮播
        looperPager.stopLoop();
    }

    @Override
    protected void initView(View rootView) {
        // 设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),1.5F);
                outRect.bottom = SizeUtils.dip2px(getContext(),1.5F);

            }
        });
        // 创建适配器
        mContentAdapter = new LinearItemContentAdapter();
        // 设置适配器
        mContentList.setAdapter(mContentAdapter);
        // 创建轮播图适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        // 设置轮播图适配器
        looperPager.setAdapter(mLooperPagerAdapter);

        // 设置refresh相关内容 尝试使用了不同于课程的smoothRefreshLayout，自动适配，无需修改源码 但效果不如课程的控件
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableRefresh(false);
//        MaterialFooter<IIndicator> materialFooter = new MaterialFooter<>(BaseApplication.getAppContext());
//        materialFooter.setStyle(STYLE_FOLLOW_PIN);
//        mRefreshLayout.setFooterView(materialFooter);
    }

    @Override
    protected void initListener() {
        mContentAdapter.setOnListItemClickListener(this);
        mLooperPagerAdapter.setOnLooperPageItemClickListener(this);
        mHomePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homePagerNestedView == null) {
                    return;
                }
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
//                LogUtils.d("", "headerHeight -->" + headerHeight);
                homePagerNestedView.setHeaderHeight(headerHeight);
                int measuredHeight = mHomePagerParent.getMeasuredHeight();
//                LogUtils.d(HomePagerFragment.this,"measuredHeight  ->" + measuredHeight);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                // 设置完后移除，防止太多
                if (measuredHeight != 0) {
                    mHomePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 没有数据，返回
                if (mLooperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                // 取模运算，防止越界
                int targetPosition = position % mLooperPagerAdapter.getDataSize();
                //切换指示器
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(HomePagerFragment.this, "触发了上拉加载更多");
                // 去加载更多内容
                if (mCategoryPagerPresenter != null) {
                    mCategoryPagerPresenter.loadMore(mMaterialId);
                }
            }
        });

    }

    /**
     * 切换指示器
     *
     * @param targetPosition
     */
    private void updateLooperIndicator(int targetPosition) {
        // 对指示器的view进行循环，如果等于当前position则显示亮点，否则显示白点
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }

    }

    @Override
    protected void initPresenter() {
        mCategoryPagerPresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
        mCategoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
//        LogUtils.d(this, "title -->" + title);
//        LogUtils.d(this, "materialId -->" + mMaterialId);
        // 加载数据
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.getContentByCategoryId(mMaterialId);
        }
        if (currentCategoryTitle != null) {
            currentCategoryTitle.setText(title);
        }
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        // 数据列表加载回来了
        mContentAdapter.setData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onNetworkError() {
        // 网络错误
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
    public void onLoadMoreError() {
        ToastUtils.showToast("网络异常,请稍后再试~");
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        ToastUtils.showToast("没有更多数据啦~");
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents) {
        // 添加到适配器数据的底部
        mContentAdapter.addData(contents);
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast("加载了" + contents.size() + "条数据");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
//        LogUtils.d(this, "looper size " + contents);
        mLooperPagerAdapter.setData(contents);

        // 中间点%数据的size 不一定为0,所以不是第一个
        // 处理一下
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetCenterPosition = (Integer.MAX_VALUE / 2) - dx;
        // 设置到中间点
        looperPager.setCurrentItem(targetCenterPosition);
//        LogUtils.d(this, "url -->" + contents.get(0).getPict_url());
        looperPointContainer.removeAllViews();

        // 添加点
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }

            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        if (mCategoryPagerPresenter != null) {
            mCategoryPagerPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    public void onItemClick(IBaseInfo item) {
        // 列表内容被点击了
        LogUtils.d(this,"item click -->" + item.getTitle());
        handleItemClick(item);
    }

    private void handleItemClick(IBaseInfo item) {
        TicketUtils.toTicketPage(getContext(),item);
    }

    @Override
    public void onLooperItemClick(IBaseInfo item) {
        // 轮播图被点击了
        LogUtils.d(this,"looper item click -->" + item.getTitle());
        handleItemClick(item);
    }
}
