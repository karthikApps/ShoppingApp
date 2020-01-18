package com.android.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.DefaultPojo;
import com.android.shoppingapp.Model.SuccessPojo;
import com.android.ecommerce.R;
import com.braintreepayments.cardform.view.CardForm;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.shoppingapp.Global.CONST.Params.PROF_ADDR_LINE;
import static com.android.shoppingapp.Global.CONST.Params.PROF_CITY;
import static com.android.shoppingapp.Global.CONST.Params.PROF_NAME;
import static com.android.shoppingapp.Global.CONST.Params.PROF_PINCODE;
import static com.android.shoppingapp.Global.CONST.Params.PROF_STATE;
import static com.android.shoppingapp.Global.CONST.Params.PROF_STREET;
import static com.android.shoppingapp.Global.SessionManager.KEY_USER_ID;

public class CheckOutActivity extends AppCompatActivity {

    @BindView(R.id.checkout_submit_txt)
    TextView checkoutSubmitTxt;

    private static final String TAG = "WishListActivity";
    SessionManager sessionManager;
    HashMap<String, String> userDetails = new HashMap<>();

    String userId;
    @BindView(R.id.parent_layout)
    RelativeLayout parentLayout;
    @BindView(R.id.radio_cash)
    RadioButton radioCash;
    @BindView(R.id.radio_online)
    RadioButton radioOnline;
    @BindView(R.id.payment_rg)
    RadioGroup paymentRg;
    @BindView(R.id.checkout_address_txt)
    TextView checkoutAddressTxt;
    @BindView(R.id.default_address_linear)
    LinearLayout defaultAddressLinear;
    @BindView(R.id.checkout_card_txt)
    TextView checkoutCardTxt;
    @BindView(R.id.card_linear)
    LinearLayout cardLinear;
    private APIInterface apiInterface;

    String paymentTypeStr = "2";
    Boolean hasDefaultCard = false, hasDefaultAddress = false;


    private AlertDialog alertDialog;

    private CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        userDetails = sessionManager.getUserDetails();

        userId = userDetails.get(KEY_USER_ID);


        HashMap<String, String> addressMap = new HashMap<>();
        addressMap.put(CONST.Params.Type, "address");

        jsonDefault(addressMap, "address");


        paymentRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb) {

                    switch (checkedId) {

                        case R.id.radio_cash:
                            paymentTypeStr = "2";
                            cardLinear.setVisibility(View.GONE);
                            break;
                        case R.id.radio_online:
                            paymentTypeStr = "1";
                            cardLinear.setVisibility(View.VISIBLE);
                            HashMap<String, String> cardMap = new HashMap<>();
                            cardMap.put(CONST.Params.Type, "card");
                            jsonDefault(cardMap, "card");
                            break;

                    }
                }

            }
        });
    }

    public void jsonAddCart(HashMap<String, String> map) {

        Call<SuccessPojo> call = apiInterface.setCheckout(sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());
                        Intent intent = new Intent(CheckOutActivity.this, PaymentSuccessActivity.class);
                        startActivity(intent);

                    } else
                        CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());

                } else
                    CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, getApplicationContext());

            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {

                Log.e("ProductList", "onFailure: " + t.getMessage());
            }
        });

    }

    public void jsonDefault(HashMap<String, String> map, String type) {

        Call<DefaultPojo> call = apiInterface.getDefault(sessionManager.getHeader(), map);
        call.enqueue(new Callback<DefaultPojo>() {
            @Override
            public void onResponse(Call<DefaultPojo> call, Response<DefaultPojo> response) {

                if (response.code() == 200) {

                    if (response.body() != null) {

                        if (response.body().getStatus()) {

                            CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());


                        } else
                            CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());


                        switch (type) {
                            case "address":
                                hasDefaultAddress = response.body().getStatus();
                                if (hasDefaultAddress) {
                                    defaultAddressLinear.setVisibility(View.VISIBLE);
                                    DefaultPojo.Data data = response.body().getData();
                                    String addressStr = data.getName() + ",\n" + data.getStreetName() + ",\n" + data.getCity() + ",\n" +
                                            data.getState() + ",\n" + data.getPincode();
                                    checkoutAddressTxt.setText(addressStr);
                                    if (paymentTypeStr.equalsIgnoreCase("1")) {
                                        HashMap<String, String> cardMap = new HashMap<>();
                                        cardMap.put(CONST.Params.Type, "card");
                                        jsonDefault(cardMap, "card");
                                    }
                                } else {

                                    alertAddAddress();

                                }
                                break;
                            case "card":
                                hasDefaultCard = response.body().getStatus();
                                if (hasDefaultCard) {
                                    cardLinear.setVisibility(View.VISIBLE);
                                    checkoutCardTxt.setText("XXXX XXXX XXXX\t" + response.body().getData().getLast4());
                                } else {
                                    alertAddCard();
                                }
                                break;
                        }

                    }


                } else
                    CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, getApplicationContext());

            }

            @Override
            public void onFailure(Call<DefaultPojo> call, Throwable t) {

                Log.e("ProductList", "onFailure: " + t.getMessage());
            }
        });

    }

    public void alertAddCard() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(CheckOutActivity.this, R.style.DialogSlideAnim_leftright);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.alert_add_card, null);
        // Set above view in alert dialog.
        builder.setView(view);

        cardForm = view.findViewById(R.id.card_form);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .mobileNumberRequired(false)
                .actionLabel("Purchase")
                .setup(this);

        cardForm.setOnCardFormSubmitListener(this::getApplicationContext);

        Button bn_add_card = view.findViewById(R.id.bn_add_card);
        bn_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> cardMap = new HashMap<>();
                cardMap.put(CONST.Params.CARD_NUM, cardForm.getCardNumber());
                cardMap.put(CONST.Params.CARD_CVV, cardForm.getCvv());
                cardMap.put(CONST.Params.CARD_MONTH, cardForm.getExpirationMonth());
                cardMap.put(CONST.Params.CARD_YEAR, cardForm.getExpirationYear());
                jsonAddAddress(cardMap, "add-card");

                alertDialog.dismiss();

            }
        });

        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void alertAddAddress() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(CheckOutActivity.this, R.style.DialogSlideAnim_leftright);
        // Get custom login form view.
        View view = getLayoutInflater().inflate(R.layout.alert_add_address, null);
        // Set above view in alert dialog.
        builder.setView(view);

        EditText alert_name_edt = view.findViewById(R.id.alert_name_edt);
        EditText alert_street_edt = view.findViewById(R.id.alert_street_edt);
        EditText alert_city_edt = view.findViewById(R.id.alert_city_edt);
        EditText alert_state_edt = view.findViewById(R.id.alert_state_edt);
        EditText alert_pincode_edt = view.findViewById(R.id.alert_pincode_edt);
        TextView profile_add_address_txt = view.findViewById(R.id.profile_add_address_txt);

        profile_add_address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alert_name_edt.getText().toString().trim().length() < 4) {
                    alert_name_edt.setError("Enter Valid Name");
                } else if (alert_street_edt.getText().toString().trim().length() < 4) {
                    alert_street_edt.setError("Enter Valid Street Name");
                } else if (alert_city_edt.getText().toString().trim().length() < 3) {
                    alert_city_edt.setError("Enter Valid City");
                } else if (alert_state_edt.getText().toString().trim().length() < 3) {
                    alert_state_edt.setError("Enter Valid State");
                } else if (alert_pincode_edt.getText().toString().trim().length() < 6) {
                    alert_pincode_edt.setError("Enter Valid PinCode");
                } else {

                    HashMap<String, String> map = new HashMap<>();
                    map.put(PROF_NAME, alert_name_edt.getText().toString().trim());
                    map.put(PROF_STREET, alert_street_edt.getText().toString().trim());
                    map.put(PROF_CITY, alert_city_edt.getText().toString().trim());
                    map.put(PROF_STATE, alert_state_edt.getText().toString().trim());
                    map.put(PROF_PINCODE, alert_pincode_edt.getText().toString().trim());
                    map.put(PROF_ADDR_LINE, alert_street_edt.getText().toString().trim());
                    jsonAddAddress(map, "add-address");
                    alertDialog.dismiss();

                }

            }
        });

        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void jsonAddAddress(HashMap<String, String> map, String type) {

        Call<SuccessPojo> call = apiInterface.addAddress(type, sessionManager.getHeader(), map);
        call.enqueue(new Callback<SuccessPojo>() {
            @Override
            public void onResponse(Call<SuccessPojo> call, Response<SuccessPojo> response) {

                if (response.code() == 200) {

                    if (response.body().getStatus()) {

                        switch (type) {

                            case "add-address":
                                HashMap<String, String> addressMap = new HashMap<>();
                                addressMap.put(CONST.Params.Type, "address");
                                jsonDefault(addressMap, "address");
                                break;
                            case "add-card":
                                HashMap<String, String> cardMap = new HashMap<>();
                                cardMap.put(CONST.Params.Type, "card");
                                jsonDefault(cardMap, "card");
                                break;

                        }


                        CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());
                    } else
                        CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());

                } else
                    CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, getApplicationContext());
            }

            @Override
            public void onFailure(Call<SuccessPojo> call, Throwable t) {

                Log.e("ProductList", "onFailure: " + t.getMessage());
            }
        });

    }

    @OnClick(R.id.checkout_submit_txt)
    public void onViewClicked() {
        if (!hasDefaultAddress) {
            alertAddAddress();
        } else if (paymentTypeStr.equalsIgnoreCase("1")) {
            if (hasDefaultCard) {
                HashMap<String, String> map = new HashMap<>();
                map.put(CONST.Params.PAYMENT_MODE, paymentTypeStr);
                jsonAddCart(map);
            }else {
                alertAddCard();
            }

        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put(CONST.Params.PAYMENT_MODE, paymentTypeStr);
            jsonAddCart(map);
        }
    }
}
