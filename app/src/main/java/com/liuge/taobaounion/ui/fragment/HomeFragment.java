               package com.liuge.taobaounion.ui.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.base.BaseFragment;
import com.liuge.taobaounion.model.domain.Categories;
import com.liuge.taobaounion.presenter.IHomePresenter;
import com.liuge.taobaounion.presenter.impl.HomePresenterImpl;
import com.liuge.taobaounion.ui.activity.IMainActivity;
import com.liuge.taobaounion.ui.activity.MainActivity;
import com.liuge.taobaounion.ui.activity.ScanQrCodeActivity;
import com.liuge.taobaounion.ui.adapter.HomePagerAdapter;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.PresenterManager;
import com.liuge.taobaounion.view.IHomeCallback;
import com.vondear.rxfeature.activity.ActivityScanerCode;

import butterknife.BindView;

/**
 * FileName: HomeFragment
 * Author: LiuGe
 * Date: 2020/8/23 23:29
 * Description: 主页fragment
 */
public class HomeFragment extends BaseFragment implements IHomeCallback {


    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;
    @BindView(R.id.home_pager)
    public ViewPager mHomePager;

    @BindView(R.id.home_search_input_box)
    public EditText mSearchInputBox;

    @BindView(R.id.scan_icon)
    public View ScanBtn;

    private IHomePresenter mHomePresenter;
    private HomePagerAdapter mHomePagerAdapter;


    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        // 初始化控件
        mTabLayout.setupWithViewPager(mHomePager);
        //
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        // 给ViewPager设置适配器
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void initListener() {
        ScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到扫码界面
                startActivity(new Intent(getContext(), ScanQrCodeActivity.class));
            }
        });
    }

    @Override
    protected void initPresenter() {
        // 创建presenter
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback(this);
        mSearchInputBox.setOnClickListener(v -> {
            // 跳转到搜索界面
            FragmentActivity activity = getActivity();
            if (activity instanceof IMainActivity) {
                ((IMainActivity) activity).switch2Search();
            }

        });
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Override
    protected void loadData() {
        // 加载数据
        mHomePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        setUpState(State.SUCCESS);
        LogUtils.d(this, "onCategoriesLoaded ...");
        // 加载的数据会回来
        if (mHomePagerAdapter != null) {
//            mHomePager.setOffscreenPageLimit(categories.getData().size());
            mHomePagerAdapter.setCategories(categories);
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
    protected void release() {
        if (mHomePresenter != null) {
            mHomePresenter.unRegisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        // 网络错误，点击了重试
        // 重新加载分类内容
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }
}
