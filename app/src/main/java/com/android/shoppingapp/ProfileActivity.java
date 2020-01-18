package com.android.shoppingapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.shoppingapp.Adapter.AddressListAdapter;
import com.android.shoppingapp.Adapter.CardListAdapter;
import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.AddressListPojo;
import com.android.shoppingapp.Model.CardListPojo;
import com.android.shoppingapp.Model.SuccessPojo;
import com.android.ecommerce.R;
import com.braintreepayments.cardform.view.CardForm;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
import static com.android.shoppingapp.Global.SessionManager.KEY_USER_EMAIL;
import static com.android.shoppingapp.Global.SessionManager.KEY_USER_ID;
import static com.android.shoppingapp.Global.SessionManager.KEY_USER_IMAGE;
import static com.android.shoppingapp.Global.SessionManager.KEY_USER_NAME;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.prod_id_txt)
    TextView prodIdTxt;
    @BindView(R.id.profile_img)
    ImageView profileImg;
    @BindView(R.id.profile_name_txt)
    TextView profileNameTxt;
    @BindView(R.id.profile_add_address_txt)
    TextView profileAddAddressTxt;
    @BindView(R.id.profile_email_txt)
    TextView profileEmailTxt;
    @BindView(R.id.list_prod)
    LinearLayout listProd;
    @BindView(R.id.parent_layout)
    LinearLayout parentLayout;

    private static final String TAG = "ProfileActivity";
    SessionManager sessionManager;
    HashMap<String, String> userDetails = new HashMap<>();
    ArrayList<AddressListPojo.Data> addrList = new ArrayList<>();
    ArrayList<CardListPojo.Data> cardList = new ArrayList<>();

    //Menu
    TextView textCartItemCount;
    //    GEOFIRE
    DatabaseReference mDatabaseReference;

    String userId, userName, userEmail;
    APIInterface apiInterface;
    @BindView(R.id.address_rv)
    RecyclerView addressRv;
    @BindView(R.id.profile_add_card_txt)
    TextView profileAddCardTxt;
    @BindView(R.id.shipping_addr_txt)
    TextView shippingAddrTxt;
    @BindView(R.id.card_txt)
    TextView cardTxt;
    @BindView(R.id.card_rv)
    RecyclerView cardRv;
    private LinearLayoutManager MyLayoutManager;
    private AddressListAdapter adapter;
    private LinearLayoutManager MyLayoutManager1;
    private CardListAdapter cardAdapter;

    private AlertDialog alertDialog;

    private CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);


        actionBar();

        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        userDetails = sessionManager.getUserDetails();

        userId = userDetails.get(KEY_USER_ID);
        userName = userDetails.get(KEY_USER_NAME);
        userEmail = userDetails.get(KEY_USER_EMAIL);

        profileNameTxt.setText(userName);
        profileEmailTxt.setText(userEmail);

        Glide.with(getApplicationContext())
                .load(userDetails.get(KEY_USER_IMAGE))
                .into(profileImg);


        MyLayoutManager = new LinearLayoutManager(getApplicationContext());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        addressRv.setLayoutManager(MyLayoutManager);
        addressRv.setHasFixedSize(false);
        addressRv.setNestedScrollingEnabled(false);
        adapter = new AddressListAdapter(this, addrList, parentLayout);
        addressRv.setAdapter(adapter);

        MyLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        MyLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        cardRv.setLayoutManager(MyLayoutManager1);
        cardRv.setHasFixedSize(false);
        cardRv.setNestedScrollingEnabled(false);
        cardAdapter = new CardListAdapter(this, cardList, parentLayout);
        cardRv.setAdapter(cardAdapter);

        jsonAddressList();
        jsonCardList();

    }

    private void jsonAddressList() {

        CommonFunctions.showProgressDialog(this);

        if (CommonFunctions.isGPSEnabled(this)) {
            Call<AddressListPojo> call = apiInterface.getAddressList(sessionManager.getHeader());
            call.enqueue(new Callback<AddressListPojo>() {
                @Override
                public void onResponse(Call<AddressListPojo> call, Response<AddressListPojo> response) {

                    CommonFunctions.removeProgressDialog();

                    if (response.code() == 200) {

                        if (response.body() != null) {
                            if (response.body().getStatus()) {
                                addrList.clear();
                                addrList.addAll(response.body().getData());
                                adapter.notifyDataChanged();

                            } else
                                CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());
                        }

                    } else
                        CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, getApplicationContext());
                }

                @Override
                public void onFailure(Call<AddressListPojo> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    CommonFunctions.removeProgressDialog();
                }
            });
        } else
            CommonFunctions.shortToast(parentLayout, "Network not found", Snackbar.LENGTH_SHORT, getApplicationContext());


    }

    private void jsonCardList() {


        if (CommonFunctions.isGPSEnabled(this)) {
            Call<CardListPojo> call = apiInterface.getCardList(sessionManager.getHeader());
            call.enqueue(new Callback<CardListPojo>() {
                @Override
                public void onResponse(Call<CardListPojo> call, Response<CardListPojo> response) {

                    CommonFunctions.removeProgressDialog();

                    if (response.code() == 200) {

                        if (response.body() != null) {
                            if (response.body().getStatus()) {
                                cardList.clear();
                                cardList.addAll(response.body().getData());
                                Log.e(TAG, "" + cardList.size());
                                cardAdapter.notifyDataChanged();

                            } else
                                CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());
                        }

                    } else
                        CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, getApplicationContext());
                }

                @Override
                public void onFailure(Call<CardListPojo> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    CommonFunctions.removeProgressDialog();
                }
            });
        } else
            CommonFunctions.shortToast(parentLayout, "Network not found", Snackbar.LENGTH_SHORT, getApplicationContext());


    }

    public void actionBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.v_lit_grey)));
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F3807A'>My Profile </font>"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.v_lit_grey));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        //setupBadge(mCartItemCount);

        getFirebaseValue();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case android.R.id.home:
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_cart:
                intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    public void getFirebaseValue() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(CONST.Params.CART).child(userDetails.get(KEY_USER_ID));
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Long> map = (HashMap<String, Long>) dataSnapshot.getValue();
                if (map != null) {

                    Log.e(TAG, "onDataChange: " + map);
                    Log.e(TAG, "onDataChange: " + map.get(CONST.Params.CartValue));
                    setupBadge(Integer.parseInt(map.get(CONST.Params.CartValue).toString()));
                } else {
                    setupBadge(0);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void setupBadge(int count) {

        if (textCartItemCount != null) {
            if (count == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(count, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void alertAddAddress() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, R.style.DialogSlideAnim_leftright);
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

                        jsonCardList();
                        jsonAddressList();

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

    public void alertAddCard() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, R.style.DialogSlideAnim_leftright);
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


    @OnClick({R.id.profile_add_address_txt, R.id.profile_add_card_txt, R.id.shipping_addr_txt, R.id.card_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile_add_address_txt:
                alertAddAddress();
                break;
            case R.id.profile_add_card_txt:
                alertAddCard();
                break;
            case R.id.shipping_addr_txt:
                break;
            case R.id.card_txt:
                break;
        }
    }
}
