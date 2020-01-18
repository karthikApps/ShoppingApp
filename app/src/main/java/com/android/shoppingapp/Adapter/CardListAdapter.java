package com.android.shoppingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.CardListPojo;
import com.android.shoppingapp.Model.SuccessPojo;
import com.android.ecommerce.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private static final String TAG = "CardListAdapter";

    private SessionManager sessionManager;
    APIInterface apiInterface;
    List<CardListPojo.Data> dataList = new ArrayList<>();
    Context context;
    LinearLayout parentLayout;
    String forceInsertStr = "0";
    String foodIdStr, qtyStr;
    private int defaultPosition;


    public CardListAdapter(Context context, List<CardListPojo.Data> foodLists, LinearLayout parentLayout) {
        this.dataList = foodLists;
        this.context = context;
        this.parentLayout = parentLayout;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardListPojo.Data data = dataList.get(position);

        String cardName = "XXXX XXXX XXXX"+data.getLast4();
        holder.rbCardDetails.setText(cardName);

        if (data.getIsDefault() == 1) {
            holder.rbCardDetails.setChecked(true);
            defaultPosition = position;
        } else
            holder.rbCardDetails.setChecked(false);

        holder.tvDeletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position==defaultPosition){
                    CommonFunctions.shortToast(parentLayout, "Can't remove default card", Snackbar.LENGTH_SHORT, context);
                }else {
                    HashMap<String,String> map = new HashMap<>();
                    map.put(CONST.Params.CARD_ID,String.valueOf(data.getId()));
                    jsonCard(map,"delete-card",position);

                    dataList.remove(position);
                    notifyDataChanged();
                }

            }
        });

        holder.rbCardDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position==defaultPosition){
                    CommonFunctions.shortToast(parentLayout, "Already It's Default card", Snackbar.LENGTH_SHORT, context);
                }else {
                    HashMap<String,String> map = new HashMap<>();
                    map.put(CONST.Params.CARD_ID,String.valueOf(data.getId()));
                    jsonCard(map,"default-card",position);
                    dataList.get(defaultPosition).setIsDefault(0);
                    dataList.get(position).setIsDefault(1);

                    notifyDataChanged();
                }

            }
        });


    }

    private void jsonCard(HashMap<String, String> map, String type, int position) {

        Call<SuccessPojo> call = apiInterface.deletCardAddress(type,sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        switch (type){
                            case "default-card":

                                break;
                            case "delete-card":

                                break;
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

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    static

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rb_card_details)
        RadioButton rbCardDetails;
        @BindView(R.id.iv_card_type)
        ImageView ivCardType;
        @BindView(R.id.tv_delet_card)
        TextView tvDeletCard;
        @BindView(R.id.card_icon_linear)
        LinearLayout cardIconLinear;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
