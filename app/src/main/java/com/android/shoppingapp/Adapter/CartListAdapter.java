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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.CartListPojo;
import com.android.shoppingapp.Model.SuccessPojo;
import com.android.shoppingapp.ProductDetailActivity;
import com.android.ecommerce.R;
import com.bumptech.glide.Glide;
import com.like.LikeButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    List<CartListPojo.Data> dataList = new ArrayList<>();
    private SessionManager sessionManager;
    APIInterface apiInterface;
    Context context;
    TextView cartSubTotalTxt;
    TextView cartCheckoutTxt;
    RelativeLayout parentLayout;

    public CartListAdapter(Context context, List<CartListPojo.Data> foodLists, TextView cartSubTotalTxt, TextView cartCheckoutTxt, RelativeLayout parentLayout) {
        this.dataList = foodLists;
        this.context = context;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        this.cartSubTotalTxt = cartSubTotalTxt;
        this.cartCheckoutTxt = cartCheckoutTxt;
        this.parentLayout = parentLayout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cart, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartListPojo.Data data = dataList.get(position);
        holder.prodNameTxt.setText(data.getProductName());
        holder.productRatingTxt.setText(String.valueOf(data.getRating()));
        holder.productDescTxt.setText(data.getDescription());
        int amt = Integer.parseInt(data.getPrice()) * data.getCartQty();
        holder.prodNewAmtTxt.setText(sessionManager.getCurrency() + "\t" + amt);

        Glide.with(context)
                .load(data.getImage())
                .into(holder.prodImg);


        holder.hotelLikeImg.setUnlikeDrawableRes(R.drawable.ic_unlike);
        holder.hotelLikeImg.setLikeDrawableRes(R.drawable.ic_like);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorAccent);
        holder.hotelLikeImg.setCircleEndColorRes(R.color.colorPrimary);
        holder.hotelLikeImg.setExplodingDotColorsRes(R.color.colorPrimary, R.color.colorAccent);

        holder.itemQtyTxt.setText(String.valueOf(data.getCartQty()));

        holder.itemAddTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data.getCartQty() < 25) {
                    data.setCartQty(data.getCartQty() + 1);
                    holder.itemQtyTxt.setText(String.valueOf(data.getCartQty()));
                    int amt = Integer.parseInt(data.getPrice()) * data.getCartQty();
                    holder.prodNewAmtTxt.setText(sessionManager.getCurrency() + "\t" + amt);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("product_id", String.valueOf(data.getId()));
                    map.put("quantity", String.valueOf(data.getCartQty()));
                    jsonAddQtyCart(map);
                }


            }
        });

        holder.itemMinusTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data.getCartQty() > 1) {
                    data.setCartQty(data.getCartQty() - 1);
                    int amt = Integer.parseInt(data.getPrice()) * data.getCartQty();
                    holder.prodNewAmtTxt.setText(sessionManager.getCurrency() + "\t" + amt);
                    holder.itemQtyTxt.setText(String.valueOf(data.getCartQty()));
                    HashMap<String, String> map = new HashMap<>();
                    map.put("product_id", String.valueOf(data.getId()));
                    map.put("quantity", String.valueOf(data.getCartQty()));
                    jsonAddQtyCart(map);
                } else {

                    CommonFunctions.shortToast(parentLayout, "It Could Remove Your Product", Snackbar.LENGTH_SHORT, context);
                }


            }
        });

        holder.listProdRemoveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", String.valueOf(data.getId()));
                map.put("type", String.valueOf("0"));
                jsonAddCart(map);

                dataList.remove(position);
                notifyDataChanged();
            }
        });

        holder.hotelLikeImg.setVisibility(View.GONE);

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
                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);
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
        @BindView(R.id.list_prod_remove_txt)
        TextView listProdRemoveTxt;
        @BindView(R.id.product_rating_txt)
        TextView productRatingTxt;
        @BindView(R.id.product_desc_txt)
        TextView productDescTxt;
        @BindView(R.id.prod_new_amt_txt)
        TextView prodNewAmtTxt;
        @BindView(R.id.item_add_txt)
        TextView itemAddTxt;
        @BindView(R.id.item_qty_txt)
        TextView itemQtyTxt;
        @BindView(R.id.item_minus_txt)
        TextView itemMinusTxt;
        @BindView(R.id.list_prod)
        LinearLayout listProd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void jsonAddQtyCart(HashMap<String, String> map) {

        Call<SuccessPojo> call = apiInterface.addCartQuantity(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        cartSubTotalTxt.setText("Sub Total :\t" + sessionManager.getCurrency() + "\t" + response.body().getSubTotal());

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
                        cartSubTotalTxt.setText("Sub Total :\t" + sessionManager.getCurrency() + "\t" + response.body().getSubTotal());
                        if (response.body().getSubTotal()<1){
                            cartCheckoutTxt.setVisibility(View.GONE);
                            cartSubTotalTxt.setVisibility(View.GONE);
                        }else {
                            cartCheckoutTxt.setVisibility(View.VISIBLE);
                            cartSubTotalTxt.setVisibility(View.VISIBLE);
                        }
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
