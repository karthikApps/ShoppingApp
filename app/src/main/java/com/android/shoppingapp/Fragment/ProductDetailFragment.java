package com.android.shoppingapp.Fragment;


import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.ProductDescPojo;
import com.android.shoppingapp.Model.SuccessPojo;
import com.android.ecommerce.R;
import com.bumptech.glide.Glide;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailFragment extends Fragment {

    private static final String TAG = "MainActivity";
    SessionManager sessionManager;
    HashMap<String, String> userDetails = new HashMap<>();

    Context context;

    APIInterface apiInterface;
    @BindView(R.id.prod_desc_name_txt)
    TextView prodDescNameTxt;
    @BindView(R.id.prod_desc_spl_txt)
    TextView prodDescSplTxt;
    @BindView(R.id.prod_desc_old_amt_txt)
    TextView prodDescOldAmtTxt;
    @BindView(R.id.prod_desc_new_amt_txt)
    TextView prodDescNewAmtTxt;
    @BindView(R.id.product_rating_txt)
    TextView productRatingTxt;
    @BindView(R.id.prod_desc_img)
    ImageView prodDescImg;
    @BindView(R.id.prod_desc_img_rv)
    RecyclerView prodDescImgRv;
    @BindView(R.id.prod_desc_txt)
    TextView prodDescTxt;
    @BindView(R.id.product_avail_txt)
    TextView productAvailTxt;
    @BindView(R.id.product_code_txt)
    TextView productCodeTxt;
    @BindView(R.id.product_weight_txt)
    TextView productWeightTxt;
    @BindView(R.id.product_free_shipping_txt)
    TextView productFreeShippingTxt;
    @BindView(R.id.item_add_txt)
    TextView itemAddTxt;
    @BindView(R.id.item_qty_txt)
    TextView itemQtyTxt;
    @BindView(R.id.item_minus_txt)
    TextView itemMinusTxt;
    Unbinder unbinder;
    @BindView(R.id.cart_count_linear)
    LinearLayout cartCountLinear;
    @BindView(R.id.add_cart_txt)
    TextView addCartTxt;

    int cartCount;
    @BindView(R.id.parent_layout)
    FrameLayout parentLayout;
    @BindView(R.id.no_stock_img)
    ImageView noStockImg;

    public ProductDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getContext();
        sessionManager = new SessionManager(getContext());
        sessionManager = new SessionManager(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        userDetails = sessionManager.getUserDetails();

        jsonProductDetail();
        return view;
    }

    public void jsonProductDetail() {

        if (CommonFunctions.isGPSEnabled(context)) {
            Call<ProductDescPojo> call = apiInterface.getProductDescription(sessionManager.getHeader(), CONST.productIdStr);
            call.enqueue(new Callback<ProductDescPojo>() {
                @Override
                public void onResponse(Call<ProductDescPojo> call, Response<ProductDescPojo> response) {

                    CommonFunctions.removeProgressDialog();

                    if (response.code() == 200) {

                        if (response.body() != null) {
                            if (response.body().getStatus()) {

                                ProductDescPojo.Data data = response.body().getData().get(0);

                                if (data != null) {
                                    if (prodDescNameTxt != null && data.getProductName() != null) {

                                        prodDescNameTxt.setText(data.getProductName());
                                    }
                                    prodDescNewAmtTxt.setText(sessionManager.getCurrency() + "\t" + (Integer.parseInt(data.getPrice()) - 10));
                                    prodDescOldAmtTxt.setText(sessionManager.getCurrency() + "\t" + data.getPrice());
                                    productAvailTxt.setText(data.getAvailable());
                                    productRatingTxt.setText("" + data.getRatingCount());
                                    productWeightTxt.setText(data.getWeight());
                                    productCodeTxt.setText(data.getProductCode());
                                    prodDescTxt.setText(data.getDescription());

                                    prodDescOldAmtTxt.setPaintFlags(prodDescOldAmtTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                                    if (data.getIsCart() == 1) {
                                        addCartTxt.setVisibility(View.GONE);
                                        cartCountLinear.setVisibility(View.VISIBLE);
                                    } else {
                                        addCartTxt.setVisibility(View.VISIBLE);
                                        cartCountLinear.setVisibility(View.GONE);
                                    }

                                    cartCount = data.getCartQty();
                                    itemQtyTxt.setText(String.valueOf(data.getCartQty()));

                                    if (data.getShippingCharge() == 0) {
                                        productFreeShippingTxt.setText("Free");
                                    } else {
                                        productFreeShippingTxt.setText(sessionManager.getCurrency() + "\t" + data.getShippingCharge());
                                    }

                                    Glide.with(context)
                                            .load(data.getImage().get(0).getImage())
                                            .into(prodDescImg);

                                    if (Integer.parseInt(data.getAvailableQuantity())<1){
                                        noStockImg.setVisibility(View.VISIBLE);
                                        addCartTxt.setVisibility(View.GONE);
                                        cartCountLinear.setVisibility(View.GONE);
                                    }else {
                                        noStockImg.setVisibility(View.GONE);
                                        if (data.getIsCart() == 1) {
                                            addCartTxt.setVisibility(View.GONE);
                                            cartCountLinear.setVisibility(View.VISIBLE);
                                        } else {
                                            addCartTxt.setVisibility(View.VISIBLE);
                                            cartCountLinear.setVisibility(View.GONE);
                                        }
                                    }
                                }

                            } else
                                CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, context);
                        }

                    } else
                        CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, context);
                }

                @Override
                public void onFailure(Call<ProductDescPojo> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    CommonFunctions.removeProgressDialog();
                }
            });
        } else
            CommonFunctions.shortToast(parentLayout, "Network not found", Snackbar.LENGTH_SHORT, context);

    }

    public void jsonAddCartQuantity(HashMap<String, String> map) {

        Call<SuccessPojo> call = apiInterface.addCartQuantity(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        itemQtyTxt.setText(String.valueOf(cartCount));

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

                        itemQtyTxt.setText(String.valueOf(cartCount));

                        if (cartCount > 0) {
                            cartCountLinear.setVisibility(View.VISIBLE);
                            addCartTxt.setVisibility(View.GONE);
                        } else {
                            cartCountLinear.setVisibility(View.GONE);
                            addCartTxt.setVisibility(View.VISIBLE);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.item_add_txt, R.id.item_minus_txt, R.id.add_cart_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_add_txt:
                cartCount++;
                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", String.valueOf(CONST.productIdStr));
                map.put("quantity", String.valueOf(cartCount));
                jsonAddCartQuantity(map);
                break;
            case R.id.item_minus_txt:
                cartCount--;
                if (cartCount < 1) {
                    HashMap<String, String> map3 = new HashMap<>();
                    map3.put("product_id", CONST.productIdStr);
                    map3.put("type", String.valueOf("0"));
                    jsonAddCart(map3);

                    cartCountLinear.setVisibility(View.GONE);
                    addCartTxt.setVisibility(View.VISIBLE);

                } else {
                    HashMap<String, String> map1 = new HashMap<>();
                    if (cartCount > 0) {
                        map1.put("product_id", String.valueOf(CONST.productIdStr));
                        map1.put("quantity", String.valueOf(cartCount));
                        jsonAddCartQuantity(map1);
                    }
                }
                break;
            case R.id.add_cart_txt:
                cartCount++;
                HashMap<String, String> map2 = new HashMap<>();
                map2.put("product_id", String.valueOf(CONST.productIdStr));
                map2.put("type", String.valueOf("1"));
                jsonAddCart(map2);
                addCartTxt.setVisibility(View.GONE);
                cartCountLinear.setVisibility(View.GONE);

                break;
        }
    }
}
