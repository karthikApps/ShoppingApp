package com.android.shoppingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.ProductListPojo;
import com.android.shoppingapp.Model.SuccessPojo;
import com.android.shoppingapp.ProductDetailActivity;
import com.android.ecommerce.R;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    List<ProductListPojo.Data> dataList = new ArrayList<>();
    private SessionManager sessionManager;
    APIInterface apiInterface;
    Context context;
    LinearLayout parentLayout;

    public ProductListAdapter(Context context, List<ProductListPojo.Data> foodLists, LinearLayout parentLayout) {
        this.dataList = foodLists;
        this.context = context;
        this.parentLayout = parentLayout;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductListPojo.Data data = dataList.get(position);
        holder.prodNameTxt.setText(data.getProductName());
        holder.productRatingTxt.setText(data.getRating());
        holder.productDescTxt.setText(data.getDescription());
        holder.prodNewAmtTxt.setText(sessionManager.getCurrency()+"\t"+data.getPrice());

        if (Integer.parseInt(data.getAvailableQuantity())<1){
            holder.stockImg.setVisibility(View.VISIBLE);
            holder.addCartTxt.setVisibility(View.GONE);
        }else {
            holder.stockImg.setVisibility(View.GONE);
            holder.addCartTxt.setVisibility(View.VISIBLE);
        }

        Glide.with(context)
                .load(data.getImage())
                .into(holder.prodImg);

        if (data.getIsCart()>0){
            holder.addCartTxt.setText("Added");
            holder.listProdSplTxt.setVisibility(View.VISIBLE);
        }else {
            holder.addCartTxt.setText("Add");

            holder.listProdSplTxt.setVisibility(View.GONE);
        }

        holder.hotelLikeImg.setUnlikeDrawableRes(R.drawable.ic_unlike);
        holder.hotelLikeImg.setLikeDrawableRes(R.drawable.ic_like);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorAccent);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorPrimary);
        holder.hotelLikeImg.setExplodingDotColorsRes(R.color.colorPrimary, R.color.colorAccent);

        holder.addCartTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.addCartTxt.getText().equals("Add")){
                    HashMap<String, String> map = new HashMap<>();
                    map.put("product_id", String.valueOf(data.getId()));
                    map.put("type", String.valueOf("1"));
                    jsonAddCart(map);
                    holder.addCartTxt.setText("Added");
                    holder.listProdSplTxt.setVisibility(View.VISIBLE);
                    dataList.get(position).setIsCart(1);
                }
            }
        });
        holder.listProdSplTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", String.valueOf(data.getId()));
                map.put("type", String.valueOf("0"));
                jsonAddCart(map);
                dataList.get(position).setIsCart(0);
                holder.addCartTxt.setText("Add");
                holder.listProdSplTxt.setVisibility(View.GONE);
            }
        });

        if (data.getIsWishlist() == 0) {
            holder.hotelLikeImg.setLiked(false);
        } else {
            holder.hotelLikeImg.setLiked(true);
        }

        holder.prodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CONST.productIdStr = String.valueOf(data.getId());
                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });
        holder.productDescTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CONST.productIdStr = String.valueOf(data.getId());
                Intent intent = new Intent(context,ProductDetailActivity.class);
                context.startActivity(intent);
            }
        });

        holder.hotelLikeImg.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", String.valueOf(data.getId()));
                map.put("type", String.valueOf("1"));
                jsonLikeUpdate(map);

                dataList.get(position).setIsWishlist(1);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", String.valueOf(data.getId()));
                map.put("type", String.valueOf("0"));
                jsonLikeUpdate(map);

                dataList.get(position).setIsWishlist(0);

            }
        });


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
        @BindView(R.id.hotel_like_img)
        LikeButton hotelLikeImg;
        @BindView(R.id.cat_id)
        TextView catId;
        @BindView(R.id.pos_id)
        TextView posId;
        @BindView(R.id.prod_name_txt)
        TextView prodNameTxt;
        @BindView(R.id.list_prod_spl_txt)
        TextView listProdSplTxt;
        @BindView(R.id.product_rating_txt)
        TextView productRatingTxt;
        @BindView(R.id.product_desc_txt)
        TextView productDescTxt;
        @BindView(R.id.prod_new_amt_txt)
        TextView prodNewAmtTxt;
        @BindView(R.id.add_cart_txt)
        TextView addCartTxt;
        @BindView(R.id.already_cart_txt)
        TextView alreadyCartTxt;
        @BindView(R.id.list_prod)
        LinearLayout listProd;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void jsonLikeUpdate(HashMap<String, String> map) {

        Call<SuccessPojo> call = apiInterface.homeLike(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                    } else
                        CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, context);

                } else
                    CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, context);

            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {

                Log.e("ProductList", "onFailure: " + t.getMessage());
            }
        });

    }

    private void jsonAddCart(HashMap<String, String> map) {

        Call<SuccessPojo> call = apiInterface.addCart(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {
                    } else
                        CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, context);

                } else
                    CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, context);

            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {

                Log.e("ProductList", "onFailure: " + t.getMessage());
            }
        });

    }

}
