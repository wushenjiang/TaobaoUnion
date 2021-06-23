package com.liuge.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.utils.LogUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * FileName: TextFlowLayout
 * Author: LiuGe
 * Date: 2020/8/28 20:38
 * Description: 悬浮text
 */
public class TextFlowLayout extends ViewGroup {

    private static final int DEFAULT_SPACE = 10;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSelfWidth;
    private int mItemHeight;
    private OnFlowTextItemClickListener mItemClickListener = null;

    public int getContentSize(){
        return mTextList.size();
    }
    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    private List<String> mTextList = new ArrayList<>();

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 去拿到相关属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FloatTextStyle);
        mItemHorizontalSpace = ta.getDimension(R.styleable.FloatTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FloatTextStyle_verticalSpace, DEFAULT_SPACE);
        ta.recycle();
//        LogUtils.d(this, "mItemHorizontalSpace -->" + mItemHorizontalSpace);
//        LogUtils.d(this, "mItemVerticalSpace -->" + mItemVerticalSpace);
    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);
        // 遍历内容
        for (String text : mTextList) {
            // 添加子view
            // LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view,this,true);
            // 等价于
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(v -> {
                if (mItemClickListener != null) {
                    mItemClickListener.onFlowItemClick(text);
                }
            });
            addView(item);
        }
    }

    // 描述所有的行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(getChildCount() == 0){
            // 没数据就直接返回不要测量
            return;
        }
        // 描述单行
        List<View> line = null;
        lines.clear();
        // 自己的宽度，减去两个padding
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
//        LogUtils.d(this, "mSelfWidth --> " + mSelfWidth);
        // 测量
        // 测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                // 不需要进行测量
                continue;
            }
            // 测量前
//            LogUtils.d(this, "before height --> " + itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            // 测量后
//            LogUtils.d(this, "after height --> " + itemView.getMeasuredHeight());

            if (line == null) {
                // 说明当前行为空，可以添加
                line = createNewLine(itemView);

            } else {
                // 判断是否可以再继续添加
                if (canBeAdd(itemView, line)) {
                    // 可以添加，添加进去
                    line.add(itemView);
                } else {
                    // 新创建一行
                    line = createNewLine(itemView);
                }
            }
        }
        // 测量自己
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5F);
        setMeasuredDimension(mSelfWidth, selfHeight);

    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行是否可以再继续添加新数据
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        // 所有已经添加的子view的宽度相加+间距((line.size() + 1)) *  mItemHorizontalSpace + item.getMeasuredWidth()
        // 条件：如果<=当前控件的宽度，则可以添加，否则不能
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            // 叠加所有子view的宽度
            totalWidth += view.getMeasuredWidth();
        }
        // 水平间距的宽度
        totalWidth += mItemHorizontalSpace * (line.size() + 1);
        LogUtils.d(this, "totalWidth -->" + totalWidth);
        // 如果<=当前控件的宽度，则可以添加，否则不能
        return totalWidth <= mSelfWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 摆放孩子
        int topOffset = (int) mItemVerticalSpace;
        for (List<View> views : lines) {
            // views:每一行
            int leftOffset = (int) mItemHorizontalSpace;
            for (View view : views) {
                // view:每一行里的每个item
                view.layout(leftOffset, topOffset, leftOffset + view.getMeasuredWidth(), topOffset + view.getMeasuredHeight());
                // 移动offset
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffset += mItemHeight + mItemVerticalSpace;
        }
    }
    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener){
        this.mItemClickListener = listener;
    }
    public interface OnFlowTextItemClickListener{
        void onFlowItemClick(String text);
    }
}
