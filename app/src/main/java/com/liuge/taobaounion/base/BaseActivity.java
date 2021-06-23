package com.liuge.taobaounion.base;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * FileName: BaseActivity
 * Author: LiuGe
 * Date: 2020/8/26 15:20
 * Description: 基类Activity
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBind;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        // 清明节灰色代码
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(0);
//        Paint paint = new Paint();
//        paint.setColorFilter(new ColorMatrixColorFilter(cm));
//        View contentContainer = getWindow().getDecorView();
//        contentContainer.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);

        mBind = ButterKnife.bind(this);
        initView();
        initListener();
        initPresenter();
    }

    protected abstract void initPresenter();

    /**
     * 需要就复写
     */
    protected void initListener() {

    }

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
        this.release();
    }

    /**
     * 子类需要再复写
     */
    protected void release() {

    }

    protected abstract int getLayoutResId();
}
