package com.liuge.taobaounion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.liuge.taobaounion.R;

/**
 * FileName: LoadingView
 * Author: LiuGe
 * Date: 2020/8/26 16:52
 * Description: 加载view
 */
public class LoadingView extends AppCompatImageView {

    private float mDegree = 0;

    private boolean mNeedRotate = true;

    public LoadingView(@NonNull Context context) {
        this(context, null);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mNeedRotate = true;
        startRotate();
    }

    private void startRotate() {
        post(new Runnable() {
            @Override
            public void run() {
                mDegree += 10;
                if (mDegree >= 360) {
                    mDegree = 0;
                }
                invalidate();
                // 判断是否要继续旋转
                // 如果不可见，或已经DetachedFromWindow就不再转动了
                if (getVisibility() != VISIBLE && !mNeedRotate) {
                    removeCallbacks(this);
                } else {
                    postDelayed(this, 10);
                }

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void stopRotate() {
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegree, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }
}
