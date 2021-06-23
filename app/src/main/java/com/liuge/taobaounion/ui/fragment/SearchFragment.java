package com.liuge.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.base.BaseFragment;
import com.liuge.taobaounion.model.domain.Histories;
import com.liuge.taobaounion.model.domain.IBaseInfo;
import com.liuge.taobaounion.model.domain.SearchRecommend;
import com.liuge.taobaounion.model.domain.SearchResult;
import com.liuge.taobaounion.presenter.ISearchPresenter;
import com.liuge.taobaounion.ui.adapter.LinearItemContentAdapter;
import com.liuge.taobaounion.ui.custom.TextFlowLayout;
import com.liuge.taobaounion.utils.KeyboardUtils;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.PresenterManager;
import com.liuge.taobaounion.utils.SizeUtils;
import com.liuge.taobaounion.utils.TicketUtils;
import com.liuge.taobaounion.utils.ToastUtils;
import com.liuge.taobaounion.view.ISearchPageCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * FileName: RedPacketFragment
 * Author: LiuGe
 * Date: 2020/8/23 23:37
 * Description: 特惠fragment
 */
public class SearchFragment extends BaseFragment implements ISearchPageCallback, TextFlowLayout.OnFlowTextItemClickListener {

    @BindView(R.id.search_history_view)
    public TextFlowLayout mHistoryView;

    @BindView(R.id.search_recommend_view)
    public TextFlowLayout mRecommendView;

    @BindView(R.id.search_recommend_container)
    public View mRecommendContainer;

    @BindView(R.id.search_history_container)
    public View mHistoryContainer;

    @BindView(R.id.search_delete_history)
    public ImageView mDeleteHistory;

    @BindView(R.id.search_result_list)
    public RecyclerView mSearchList;

    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout mRefreshContainer;

    @BindView(R.id.search_btn)
    public TextView mSearchBtn;

    @BindView(R.id.search_clean_btn)
    public ImageView mCleanBtn;

    @BindView(R.id.search_input_box)
    public EditText mSearchInputBox;

    private ISearchPresenter mSearchPresenter;
    private LinearItemContentAdapter mSearchResultAdapter;

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        // 获取搜索推荐词
        mSearchPresenter.getRecommendWords();
//        mSearchPresenter.doSearch("键盘");
        mSearchPresenter.getHistories();

    }

    @Override
    protected void initListener() {
        mHistoryView.setOnFlowTextItemClickListener(this);
        mRecommendView.setOnFlowTextItemClickListener(this);
        // 发起搜索
        mSearchBtn.setOnClickListener(v -> {
            // 如果有内容，搜索
            if(hasInput(false)){
                // 发起搜索
                if (mSearchPresenter != null) {
//                        mSearchPresenter.doSearch(mSearchInputBox.getText().toString().trim());
                    toSearch(mSearchInputBox.getText().toString().trim());
                    KeyboardUtils.hide(getContext(),v);
                }
            }else{
                // 隐藏键盘
                KeyboardUtils.hide(getContext(),v);
            }
            // 如果没内容 则取消
        });
        // 清除输入框里的内容
        mCleanBtn.setOnClickListener(v -> {
            mSearchInputBox.setText("");
            // 回到历史记录界面
            switch2HistoryPage();
        });
        // 监听输入框的内容变化
        mSearchInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 变化时候的通知
                // 如果长度不为0，那么显示删除按钮
                mCleanBtn.setVisibility(hasInput(true) ? View.VISIBLE : View.GONE);
                mSearchBtn.setText(hasInput(false) ? "搜索" : "取消");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchInputBox.setOnEditorActionListener((v, actionId, event) -> {
//                LogUtils.d(SearchFragment.this,"actionId -->" + actionId);
            if (actionId == EditorInfo.IME_ACTION_SEARCH && mSearchPresenter != null) {
                // 判断拿到的内容是否为空
                String keyword = v.getText().toString().trim();
                if (TextUtils.isEmpty(keyword)) {
                    return false;
                }
//                    LogUtils.d(SearchFragment.this,"input text -->" + v.getText());
                // 发起搜索
                toSearch(keyword);
//                    mSearchPresenter.doSearch(keyword);
            }
            return false;
        });

        mDeleteHistory.setOnClickListener(v -> {
            // 删除历史记录
            mSearchPresenter.delHistories();
        });

        mRefreshContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                // 去加载更多内容
                if (mSearchPresenter != null) {
                    mSearchPresenter.loadMore();
                }
            }
        });

        mSearchResultAdapter.setOnListItemClickListener(item -> {
            // 搜索列表内容被点击了
            TicketUtils.toTicketPage(getContext(), item);
        });
    }

    /**
     * 切换到历史和推荐界面
     */
    private void switch2HistoryPage() {
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
        // 推荐
        if (mRecommendView.getContentSize() != 0) {
            mRecommendContainer.setVisibility(View.VISIBLE);
        }else{
            mRecommendContainer.setVisibility(View.GONE);
        }
        // 隐藏内容
        mRefreshContainer.setVisibility(View.GONE);
    }

    private boolean hasInput(boolean containSpace) {
        if (containSpace) {
            return mSearchInputBox.getText().toString().length() > 0;
        } else {
            return mSearchInputBox.getText().toString().trim().length() > 0;
        }
    }

    @Override
    protected void onRetryClick() {
        // 重新加载
        if (mSearchPresenter != null) {
            mSearchPresenter.research();
        }
    }

    @Override
    protected void release() {
        super.release();
        if (mSearchPresenter != null) {
            mSearchPresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected void initView(View rootView) {
        // 设置布局管理器
        mSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        // 设置适配器
        mSearchResultAdapter = new LinearItemContentAdapter();
        mSearchList.setAdapter(mSearchResultAdapter);
        mSearchList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 1.5F);
                outRect.bottom = SizeUtils.dip2px(getContext(), 1.5F);

            }
        });
        // 设置刷新控件
        mRefreshContainer.setEnableLoadmore(true);
        mRefreshContainer.setEnableRefresh(false);
        mRefreshContainer.setEnableOverScroll(true);
    }

    @Override
    public void onHistoryLoaded(Histories histories) {
        // 设置成显示成功的界面
        setUpState(State.SUCCESS);
        if (histories == null || histories.getHistories().size() == 0) {
            mHistoryContainer.setVisibility(View.GONE);
        } else {
            mHistoryContainer.setVisibility(View.VISIBLE);
            mHistoryView.setTextList(histories.getHistories());
        }
    }

    @Override
    public void onHistoryDeleted() {
        // 更新历史记录
        if (mSearchPresenter != null) {
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
        // 隐藏掉历史记录和推荐
        mRecommendContainer.setVisibility(View.GONE);
        mHistoryContainer.setVisibility(View.GONE);
        // 显示搜索结果
        mRefreshContainer.setVisibility(View.VISIBLE);
        // 设置数据
        try {
            mSearchResultAdapter.setData(result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data());
        } catch (Exception e) {
            e.printStackTrace();
            // 切换到搜索内容为空
            setUpState(State.EMPTY);
        }

    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        // 加载更多的结果
        // 拿到结果，添加到适配器的尾部
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> moreData = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(moreData);
        // 显示加载了多少条
        ToastUtils.showToast("加载了" + moreData.size() + "条数据~");
        mRefreshContainer.finishLoadmore();
    }

    @Override
    public void onMoreLoadedError() {
        ToastUtils.showToast("网络异常，请稍后重试");
        mRefreshContainer.finishLoadmore();

    }

    @Override
    public void onMoreLoadedEmpty() {
        ToastUtils.showToast("已经没有更多内容了~");
        mRefreshContainer.finishLoadmore();
    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        // 设置成显示成功的界面
        setUpState(State.SUCCESS);
//        LogUtils.d(this, "recommendWords size -->" + recommendWords.size());
        List<String> recommendKeywords = new ArrayList<>();
        for (SearchRecommend.DataBean item : recommendWords) {
            recommendKeywords.add(item.getKeyword());
        }
        if (recommendWords == null || recommendWords.size() == 0) {
            mRecommendContainer.setVisibility(View.GONE);
        } else {
            mRecommendView.setTextList(recommendKeywords);
            mRecommendContainer.setVisibility(View.VISIBLE);
        }
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
    public void onFlowItemClick(String text) {
        // 发起搜索
        toSearch(text);
    }

    private void toSearch(String text) {
        if (mSearchPresenter != null) {
            mSearchList.scrollToPosition(0);
            mSearchInputBox.setText(text);
            mSearchInputBox.setFocusable(true);
            mSearchInputBox.requestFocus();
            mSearchInputBox.setSelection(text.length());
            mSearchPresenter.doSearch(text);
        }
    }
}
