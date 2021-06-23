package com.liuge.taobaounion.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.model.domain.IBaseInfo;
import com.liuge.taobaounion.model.domain.OnSellContent;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.UrlUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * FileName: OnSellPageContentAdapter
 * Author: LiuGe
 * Date: 2020/8/27 19:30
 * Description: 特惠的adapter
 */
public class OnSellPageContentAdapter extends RecyclerView.Adapter<OnSellPageContentAdapter.InnerHolder> {

    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnSellPageItemClickListener mContentItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        // 绑定数据
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean = mData.get(position);
        holder.setData(mapDataBean);
        // 设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentItemClickListener != null) {
                    mContentItemClickListener.onSellItemClick(mapDataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(OnSellContent content) {
        mData.clear();
        mData.addAll(content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();
    }

    /**
     * 加载更多的内容
     *
     * @param content
     */
    public void onLoadMore(OnSellContent content) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreData = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        // 原数据的长度
        int oldSize = mData.size();
        this.mData.addAll(moreData);
        notifyItemChanged(oldSize - 1, moreData.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.on_sell_cover)
        public ImageView cover;

        @BindView(R.id.on_sell_title)
        public TextView title;

        @BindView(R.id.on_sell_original_price_tv)
        public TextView originalPriceTv;

        @BindView(R.id.on_sell_off_price_tv)
        public TextView offPriceTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean) {
            title.setText(mapDataBean.getTitle());
//            LogUtils.d(this, "pic url -->" + mapDataBean.getPict_url());
            String coverPath = UrlUtils.getCoverPath(mapDataBean.getPict_url());
            Glide.with(cover.getContext()).load(coverPath).into(cover);
            String originalPrice = mapDataBean.getZk_final_price();
            originalPriceTv.setText("￥" + originalPrice);
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            int couponAmount = mapDataBean.getCoupon_amount();
            float originalPriceFloat = Float.parseFloat(originalPrice);
            float finalPrise = originalPriceFloat - couponAmount;
            offPriceTv.setText(" 券后价：" + String.format("%.2f", finalPrise));
        }
    }
    public void setOnSellPageItemClickListener (OnSellPageItemClickListener listener){
        this.mContentItemClickListener = listener;
    }
    public interface OnSellPageItemClickListener{
        void onSellItemClick(IBaseInfo data);
    }
}
