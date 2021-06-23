package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;

/**
 * FileName: TbNestedScrollView
 * Author: LiuGe
 * Date: 2020/8/25 22:04
 * Description: 自定义NestedScrollView
 */
public class TbNestedScrollView extends NestedScrollView {
    private static final String TAG = "TbNestedScrollView";
    private int mHeaderHeight = 0;
    private int originScroll = 0;
    private RecyclerView mRecyclerView;

    public TbNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderHeight(int headerHeight) {
        this.mHeaderHeight = headerHeight;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//        LogUtils.d(this,"dy -->" + dy);
        if (target instanceof RecyclerView) {
            this.mRecyclerView = (RecyclerView) target;
        }
        if (originScroll < mHeaderHeight) {
            scrollBy(dx, dy);
            // 把dx和dy自己消费掉，不交给孩子处理
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        this.originScroll = t;
//        LogUtils.d(this,"vertical -->" + t);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 判断子类是否已经滑动到了底部
     *
     * @return
     */
    public boolean isInBottom() {
        if (mRecyclerView != null) {
            boolean isBottom = !mRecyclerView.canScrollVertically(1);
            return isBottom;
        }
        return false;
    }
}
