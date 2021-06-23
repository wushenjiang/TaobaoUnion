package com.liuge.taobaounion.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.liuge.taobaounion.R;
import com.liuge.taobaounion.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FileName: TestActivity
 * Author: LiuGe
 * Date: 2020/8/24 0:04
 * Description: 测试
 */
public class TestActivity extends Activity {
    @BindView(R.id.test_navigation_bar)
    public RadioGroup navigationBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        navigationBar.setOnCheckedChangeListener((group, checkedId) -> {
//            LogUtils.d(TestActivity.class,"checkId --> " + checkedId);
            switch (checkedId) {
                case R.id.test_home:
                    LogUtils.d(TestActivity.class,"切换到首页");
                    break;
                case R.id.test_recommend:
                    LogUtils.d(TestActivity.class,"切换到精选");
                    break;
                case R.id.test_red_packet:
                    LogUtils.d(TestActivity.class,"切换到红包");
                    break;
                case R.id.test_search:
                    LogUtils.d(TestActivity.class,"切换到搜索");
                    break;
            }
        });
    }
}
