package com.android.shoppingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.HistoryPojo;
import com.android.shoppingapp.ProductDetailActivity;
import com.android.ecommerce.R;
import com.bumptech.glide.Glide;
import com.like.LikeButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<HistoryPojo.Data> dataList = new ArrayList<>();

    private SessionManager sessionManager;
    APIInterface apiInterface;
    Context context;

    public HistoryAdapter(Context context, List<HistoryPojo.Data> foodLists) {
        this.dataList = foodLists;
        this.context = context;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HistoryPojo.Data data = dataList.get(position);
        holder.prodNameTxt.setText(data.getProductName());
        holder.productRatingTxt.setText(""+data.getRating());
        int amt = Integer.parseInt(data.getPrice()) * Integer.parseInt(data.getQty());
        holder.prodNewAmtTxt.setText(sessionManager.getCurrency() + "\t" + amt);

        Glide.with(context)
                .load(data.getImage())
                .into(holder.prodImg);


        holder.hotelLikeImg.setUnlikeDrawableRes(R.drawable.ic_unlike);
        holder.hotelLikeImg.setLikeDrawableRes(R.drawable.ic_like);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorAccent);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorPrimary);
        holder.hotelLikeImg.setExplodingDotColorsRes(R.color.colorPrimary, R.color.colorAccent);


        holder.hotelLikeImg.setVisibility(View.GONE);

        holder.prodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CONST.productIdStr = String.valueOf(data.getProductId());
                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });
        holder.productDescTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CONST.productIdStr = String.valueOf(data.getProductId());
                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });

        holder.trackStatusTxt.setText(data.getStatus());
        holder.listProdQtyTxt.setText("Quantity : "+data.getQty());


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.prod_id_txt)
        TextView prodIdTxt;
        @BindView(R.id.prod_img)
        ImageView prodImg;
        @BindView(R.id.stock_img)
        ImageView stockImg;
        @BindView(R.id.hotel_like_img)
        LikeButton hotelLikeImg;
        @BindView(R.id.cat_id)
        TextView catId;
        @BindView(R.id.pos_id)
        TextView posId;
        @BindView(R.id.prod_name_txt)
        TextView prodNameTxt;
        @BindView(R.id.list_prod_qty_txt)
        TextView listProdQtyTxt;
        @BindView(R.id.product_rating_txt)
        TextView productRatingTxt;
        @BindView(R.id.product_desc_txt)
        TextView productDescTxt;
        @BindView(R.id.prod_new_amt_txt)
        TextView prodNewAmtTxt;
        @BindView(R.id.track_status_txt)
        TextView trackStatusTxt;
        @BindView(R.id.list_prod)
        LinearLayout listProd;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }




}
