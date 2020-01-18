package com.android.shoppingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.LatestCollectionPojo;
import com.android.shoppingapp.ProductDetailActivity;
import com.android.ecommerce.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LatestCollectionAdapter extends RecyclerView.Adapter<LatestCollectionAdapter.ViewHolder> {

    private static final String TAG = "FoodCategoryAdapter";
    private SessionManager sessionManager;
    APIInterface apiInterface;
    List<LatestCollectionPojo.Data> dataList = new ArrayList<>();
    Context context;
    String forceInsertStr = "0";
    String foodIdStr, qtyStr;


    public LatestCollectionAdapter(Context context, List<LatestCollectionPojo.Data> foodLists) {
        this.dataList = foodLists;
        this.context = context;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_main, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LatestCollectionPojo.Data data = dataList.get(position);

        holder.listProdNameTxt.setText(data.getProductName());
        holder.listProdAmtTxt.setText(sessionManager.getCurrency()+"\t"+data.getPrice());
        holder.listProdRatingTxt.setText(data.getRating());

        holder.listProdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CONST.productIdStr = String.valueOf(data.getProductId());
                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });

        Glide.with(context)
                .load(data.getImage())
                .into(holder.listProdImg);

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
        @BindView(R.id.list_prod_img)
        ImageView listProdImg;
        @BindView(R.id.list_prod_name_txt)
        TextView listProdNameTxt;
        @BindView(R.id.list_prod_amt_txt)
        TextView listProdAmtTxt;
        @BindView(R.id.list_prod_rating_txt)
        TextView listProdRatingTxt;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
