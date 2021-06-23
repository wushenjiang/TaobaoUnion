package com.liuge.taobaounion.ui.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liuge.taobaounion.R;
import com.liuge.taobaounion.model.domain.RecommendPageCategory;
import com.liuge.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: RecommendPageLeftAdapter
 * Author: LiuGe
 * Date: 2020/8/26 19:52
 * Description: 精选左侧列表适配器
 */
public class RecommendPageLeftAdapter extends RecyclerView.Adapter<RecommendPageLeftAdapter.InnerHolder> {

    private List<RecommendPageCategory.DataBean> mData = new ArrayList<>();

    private int mCurrentSelectedPosition = 0;
    private OnLeftItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recomend_page_left, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        TextView itemTv = holder.itemView.findViewById(R.id.left_category_tv);
        if (mCurrentSelectedPosition == position) {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.colorEfEEEEE, null));
        } else {
            itemTv.setBackgroundColor(itemTv.getResources().getColor(R.color.white, null));
        }
        RecommendPageCategory.DataBean dataBean = mData.get(position);
        itemTv.setText(dataBean.getFavorites_title());
        holder.itemView.setOnClickListener(v -> {
            if (mCurrentSelectedPosition != position && mItemClickListener != null) {
                // 修改当前选中的位置
                mCurrentSelectedPosition = position;
                mItemClickListener.onLeftItemClick(dataBean);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置数据
     *
     * @param categories
     */
    public void setData(RecommendPageCategory categories) {
        List<RecommendPageCategory.DataBean> data = categories.getData();
        if (data != null) {
            this.mData.clear();
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
        if(mData.size() > 0){
            mItemClickListener.onLeftItemClick(mData.get(mCurrentSelectedPosition));
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnLeftItemClickListener(OnLeftItemClickListener listener) {

        this.mItemClickListener = listener;
    }

    public interface OnLeftItemClickListener {
        void onLeftItemClick(RecommendPageCategory.DataBean item);
    }
}
