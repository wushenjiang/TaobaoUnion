package com.liuge.taobaounion.ui.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.base.BaseActivity;
import com.liuge.taobaounion.base.BaseFragment;
import com.liuge.taobaounion.ui.fragment.HomeFragment;
import com.liuge.taobaounion.ui.fragment.RecommendFragment;
import com.liuge.taobaounion.ui.fragment.OnSellFragment;
import com.liuge.taobaounion.ui.fragment.SearchFragment;
import com.liuge.taobaounion.utils.LogUtils;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * FileName: MainActivity
 * Author: LiuGe
 * Date: 2020/8/26 17:49
 * Description: 主activity
 */
public class MainActivity extends BaseActivity implements IMainActivity {

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private RecommendFragment mRecommendFragment;
    private OnSellFragment mOnSellFragment;
    private SearchFragment mSearchFragment;
    private FragmentManager mFm;
    private Unbinder mBind;

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initListener() {
        initEvent();
    }

    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    /**
     * 跳转到搜索界面
     */
    public void switch2Search(){
//        switchFragment(mSearchFragment);
        // 切换导航栏的选中项
        mNavigationView.setSelectedItemId(R.id.search);

    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mRecommendFragment = new RecommendFragment();
        mOnSellFragment = new OnSellFragment();
        mSearchFragment = new SearchFragment();
        mFm = getSupportFragmentManager();

        switchFragment(mHomeFragment);
    }

    private void initEvent() {
        mNavigationView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home) {
                LogUtils.d(this, "切换到首页");
                switchFragment(mHomeFragment);
            } else if (item.getItemId() == R.id.recommend) {
                LogUtils.d(this, "切换到精选");
                switchFragment(mRecommendFragment);
            } else if (item.getItemId() == R.id.red_packet) {
                LogUtils.d(this, "切换到特惠");
                switchFragment(mOnSellFragment);
            } else if (item.getItemId() == R.id.search) {
                LogUtils.d(this, "切换到搜索");
                switchFragment(mSearchFragment);
            }
            return true;
        });
    }

    /**
     * 上一次显示的fragment
     */
    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        // 如果是同一个fragment则返回
        if(lastOneFragment == targetFragment){
            return;
        }
        // 修改成add和hide的方式来控制fragment的切换
        FragmentTransaction fragmentTransaction = mFm.beginTransaction();
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.main_page_container, targetFragment);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        // 如果不是同一个fragment则删除
        if (lastOneFragment != null ) {
            fragmentTransaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
//        fragmentTransaction.replace(R.id.main_page_container,targetFragment);
        fragmentTransaction.commit();

    }
}
