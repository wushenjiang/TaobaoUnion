package com.liuge.taobaounion.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.liuge.taobaounion.R;
import com.liuge.taobaounion.model.domain.IBaseInfo;
import com.liuge.taobaounion.model.domain.RecommendContent;
import com.liuge.taobaounion.utils.LogUtils;
import com.liuge.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.liuge.taobaounion.utils.Constants.SUCCESS_CODE;

/**
 * FileName: RecommendPageContentAdapter
 * Author: LiuGe
 * Date: 2020/8/26 20:15
 * Description: 精选右侧数据适配器
 */
public class RecommendPageContentAdapter extends RecyclerView.Adapter<RecommendPageContentAdapter.InnerHolder> {

    private List<RecommendContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnRecommendPageContentItemClickListener mContentItemClickListener = null;
    // 用map存储下每个item对应的是否有优惠卷
    private Map<Integer,Boolean> mHasOffSaleMap  = new HashMap<>();

    @NonNull
    @Override
    public RecommendPageContentAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendPageContentAdapter.InnerHolder holder, int position) {
        // 绑定数据
        RecommendContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData = mData.get(position);
        holder.setData(itemData,position);
        holder.itemView.setOnClickListener(v -> {
            if (mContentItemClickListener != null) {
                Boolean hasOffSale = mHasOffSaleMap.get(position);
                mContentItemClickListener.onContentItemClick(itemData,hasOffSale);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(RecommendContent content) {
        if (content.getCode() == SUCCESS_CODE) {
            List<RecommendContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> uatm_tbk_item = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
            this.mData.clear();
            this.mData.addAll(uatm_tbk_item);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recommend_cover)
        public ImageView cover;

        @BindView(R.id.recommend_title)
        public TextView title;

        @BindView(R.id.recommend_off_prise)
        public TextView offPriseTv;

        @BindView(R.id.recommend_buy_btn)
        public TextView buyBtn;

        @BindView(R.id.recommend_origin_prise)
        public TextView originalPriseTv;
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(RecommendContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean itemData, int position) {
            String pict_url = itemData.getPict_url();
            if(pict_url != null){
                Glide.with(itemView.getContext()).load(UrlUtils.getCoverPath(pict_url)).into(cover);
            }
            title.setText(itemData.getTitle());
            if (TextUtils.isEmpty(itemData.getCoupon_click_url())) {
                originalPriseTv.setText("晚啦，没有优惠券啦");
                mHasOffSaleMap.put(position,false);
                buyBtn.setVisibility(View.GONE);
            }else{
                originalPriseTv.setText("原价: " + itemData.getZk_final_price());
                mHasOffSaleMap.put(position,true);
            }
            if (TextUtils.isEmpty(itemData.getCoupon_info())) {
                offPriseTv.setVisibility(View.GONE);
                mHasOffSaleMap.put(position,false);
            }else{
                offPriseTv.setVisibility(View.VISIBLE);
                offPriseTv.setText(itemData.getCoupon_info());
                mHasOffSaleMap.put(position,true);
            }
        }
    }
    public void  setOnRecommendPageContentItemClickListener(OnRecommendPageContentItemClickListener listener){
        this.mContentItemClickListener = listener;
    }
    public interface OnRecommendPageContentItemClickListener{
        void onContentItemClick(IBaseInfo item, boolean hasOffSale);
    }
}
