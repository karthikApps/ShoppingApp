package com.android.shoppingapp.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.shoppingapp.Adapter.ProductReviewAdapter;
import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.ProdReviewPojo;
import com.android.ecommerce.R;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.shoppingapp.Global.SessionManager.KEY_USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {


    @BindView(R.id.product_list_rv)
    RecyclerView productListRv;
    Unbinder unbinder;

    private static final String TAG = "RelatedItemFragment";
    SessionManager sessionManager;
    HashMap<String, String> userDetails = new HashMap<>();
    Context context;
    ArrayList<ProdReviewPojo.Rating> prodList = new ArrayList<>();
    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;

    private LinearLayoutManager MyLayoutManager;
    ProductReviewAdapter adapter;
    private APIInterface apiInterface;
    private String userId;

    public ReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_related_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        context = getContext();

        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        userDetails = sessionManager.getUserDetails();

        userId = userDetails.get(KEY_USER_ID);

        MyLayoutManager = new LinearLayoutManager(context);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        productListRv.setLayoutManager(MyLayoutManager);
        adapter = new ProductReviewAdapter(context, prodList);
        productListRv.setAdapter(adapter);

        jsonGetReviewList();

        return view;
    }

    private void jsonGetReviewList() {

        CommonFunctions.showProgressDialog(context);

        if (CommonFunctions.isGPSEnabled(context)) {
            Call<ProdReviewPojo> call = apiInterface.getProductReview(sessionManager.getHeader(), CONST.productIdStr);
            call.enqueue(new Callback<ProdReviewPojo>() {
                @Override
                public void onResponse(Call<ProdReviewPojo> call, Response<ProdReviewPojo> response) {

                    CommonFunctions.removeProgressDialog();

                    if (response.code() == 200) {

                        if (response.body() != null) {
                            if (response.body().getStatus()) {
                                prodList.clear();
                                prodList.addAll(response.body().getRating());
                                adapter.notifyDataChanged();

                            } else
                                CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, context);
                        }

                    } else
                        CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, context);
                }

                @Override
                public void onFailure(Call<ProdReviewPojo> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    CommonFunctions.removeProgressDialog();
                }
            });
        } else
            CommonFunctions.shortToast(parentLayout, "Network not found", Snackbar.LENGTH_SHORT, context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
