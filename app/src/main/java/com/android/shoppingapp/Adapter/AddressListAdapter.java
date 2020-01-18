package com.android.shoppingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.AddressListPojo;
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

public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.ViewHolder> {

    private static final String TAG = "AddressListAdapter";

    private SessionManager sessionManager;
    APIInterface apiInterface;
    List<AddressListPojo.Data> dataList = new ArrayList<>();
    Context context;
    LinearLayout parentLayout;
    String forceInsertStr = "0";
    String foodIdStr, qtyStr;
    int defaultPosition;


    public AddressListAdapter(Context context, List<AddressListPojo.Data> foodLists, LinearLayout parentLayout) {
        this.dataList = foodLists;
        this.context = context;
        this.parentLayout = parentLayout;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_address, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AddressListPojo.Data data = dataList.get(position);

        String addressStr = data.getName() + ",\n" + data.getStreetName() + ",\n" + data.getCity() + ",\n" +
                data.getState() + ",\n" + data.getPincode();
        holder.rbAddressDetails.setText(addressStr);

        if (data.getIsDefault() == 1) {
            holder.rbAddressDetails.setChecked(true);
            defaultPosition = position;
        } else
            holder.rbAddressDetails.setChecked(false);

        holder.listAddressRemoveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == defaultPosition) {
                    CommonFunctions.shortToast(parentLayout, "Can't remove default Address", Snackbar.LENGTH_SHORT, context);
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put(CONST.Params.ADDRESS_ID, String.valueOf(data.getId()));
                    jsonAddress(map, "delete-address", position);
                    dataList.remove(position);
                    notifyDataChanged();
                }

            }
        });

        holder.rbAddressDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position==defaultPosition){
                    CommonFunctions.shortToast(parentLayout, "Already It's Default card", Snackbar.LENGTH_SHORT, context);
                }else {
                    HashMap<String,String> map = new HashMap<>();
                    map.put(CONST.Params.ADDRESS_ID,String.valueOf(data.getId()));
                    jsonCard(map,"default-address",position);
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
                            case "default-address":

                                break;
                            case "delete-card":

                                break;
                        }
                        CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, context);

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

    private void jsonAddress(HashMap<String, String> map, String type, int position) {

        Call<SuccessPojo> call = apiInterface.deletCardAddress(type, sessionManager.getHeader(), map);
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

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rb_address_details)
        RadioButton rbAddressDetails;
        @BindView(R.id.list_address_remove_txt)
        TextView listAddressRemoveTxt;
        @BindView(R.id.list_address_edit_txt)
        TextView listAddressEditTxt;
        @BindView(R.id.card_icon_linear)
        LinearLayout cardIconLinear;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
