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
import com.android.shoppingapp.Model.CategoryPojo;
import com.android.shoppingapp.ProductListActivity;
import com.android.ecommerce.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    List<CategoryPojo.Data> dataList = new ArrayList<>();


    private SessionManager sessionManager;
    APIInterface apiInterface;
    Context context;

    public CategoryAdapter(Context context, List<CategoryPojo.Data> foodLists) {
        this.dataList = foodLists;
        this.context = context;
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CategoryPojo.Data data = dataList.get(position);
       holder.listCategoryNameTxt.setText(data.getCategoryName());

        Glide.with(context)
                .load(data.getImage())
                .into(holder.listCategoryImg);

        holder.listCategoryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra(CONST.Params.CATEGORY,String.valueOf(data.getId()));
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_category_img)
        ImageView listCategoryImg;
        @BindView(R.id.list_category_name_txt)
        TextView listCategoryNameTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
