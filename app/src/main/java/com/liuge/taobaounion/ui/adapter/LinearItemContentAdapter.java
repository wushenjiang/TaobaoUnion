package com.liuge.taobaounion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.model.domain.HomePagerContent;
import com.liuge.taobaounion.model.domain.IBaseInfo;
import com.liuge.taobaounion.model.domain.ILinearItemInfo;
import com.liuge.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FileName: HomePagerContentAdapter
 * Author: LiuGe
 * Date: 2020/8/24 21:57
 * Description: 首页内容的适配器
 */
public class LinearItemContentAdapter extends RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder> {

    List<ILinearItemInfo> mData = new ArrayList<>();
    private onListItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LinearItemContentAdapter.InnerHolder holder, int position) {
        ILinearItemInfo dataBean = mData.get(position);
        // 设置数据
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        // 添加之前拿到原来的size
        int olderSize = mData.size();
        mData.addAll(contents);
        // 更新UI
        notifyItemRangeChanged(olderSize,contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goods_cover)
        public ImageView coverIv;
        @BindView(R.id.goods_title)
        public TextView title;
        @BindView(R.id.goods_off_price)
        public TextView offPrise;
        @BindView(R.id.goods_after_off_price)
        public TextView finalPriceTv;
        @BindView(R.id.goods_original_price)
        public TextView originalPriceTv;
        @BindView(R.id.goods_sell_count)
        public TextView sellCountTv;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ILinearItemInfo dataBean) {
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());
            ViewGroup.LayoutParams layoutParams = coverIv.getLayoutParams();
            int width = layoutParams.width;
            int height = layoutParams.height;
            // 这里由于计算后不是整数出问题，设置成了100
            int coverSize = 100;
//            LogUtils.d(this,"url -->" + dataBean.getPict_url());
            String cover = dataBean.getCover();
            if (!TextUtils.isEmpty(cover)) {
                String coverPath = UrlUtils.getCoverPath(dataBean.getCover());
                Glide.with(itemView.getContext()).load(coverPath).into(this.coverIv);
            }else{
                coverIv.setImageResource(R.mipmap.ic_launcher);
            }
//            LogUtils.d(HomePagerContentAdapter.this,"coverPath" + coverPath);
            String finalPrice = dataBean.getFinalPrice();
            long couponAmount = dataBean.getCouponAmount();
//            LogUtils.d(this,"final price -->" + finalPrice);
            float resultPrise = Float.parseFloat(finalPrice) - couponAmount;
//            LogUtils.d(this,"result prise -->"  + resultPrise);
            finalPriceTv.setText(String.format("%.2f",resultPrise));
            offPrise.setText(String.format(context.getString(R.string.text_goods_off_price),couponAmount));
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriceTv.setText(String.format(context.getString(R.string.text_goods_original_prise),finalPrice));
            sellCountTv.setText(String.format(context.getString(R.string.text_goods_sell_count),dataBean.getVolume()));
        }
    }
    public void setOnListItemClickListener(onListItemClickListener listener){
        this.mItemClickListener = listener;
    }
    public interface onListItemClickListener{
        void onItemClick(IBaseInfo item);
    }
}
