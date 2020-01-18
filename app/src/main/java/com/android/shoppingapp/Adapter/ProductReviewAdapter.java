package com.android.shoppingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.ProdReviewPojo;
import com.android.ecommerce.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductReviewAdapter extends RecyclerView.Adapter<ProductReviewAdapter.ViewHolder> {

    List<ProdReviewPojo.Rating> dataList = new ArrayList<>();
    private SessionManager sessionManager;
    APIInterface apiInterface;
    Context context;

    public ProductReviewAdapter(Context context, List<ProdReviewPojo.Rating> ratingLists) {
        this.dataList = ratingLists;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProdReviewPojo.Rating rating = dataList.get(position);

        holder.prodNameTxt.setText(rating.getName());
        holder.listProdSplTxt.setText(String.valueOf(rating.getRating()));
        holder.productDescTxt.setText(rating.getComments());

        Glide.with(context)
                .load(rating.getProfileImage())
                .into(holder.prodImg);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    static

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.prod_id_txt)
        TextView prodIdTxt;
        @BindView(R.id.prod_img)
        ImageView prodImg;
        @BindView(R.id.stock_img)
        ImageView stockImg;
        @BindView(R.id.cat_id)
        TextView catId;
        @BindView(R.id.pos_id)
        TextView posId;
        @BindView(R.id.prod_name_txt)
        TextView prodNameTxt;
        @BindView(R.id.list_prod_spl_txt)
        TextView listProdSplTxt;
        @BindView(R.id.product_desc_txt)
        TextView productDescTxt;
        @BindView(R.id.list_prod)
        LinearLayout listProd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
