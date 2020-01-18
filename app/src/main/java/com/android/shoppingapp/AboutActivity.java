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
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.DescriptionPojo;
import com.android.ecommerce.R;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.shoppingapp.Global.SessionManager.KEY_USER_ID;

public class AboutActivity extends AppCompatActivity {


    @BindView(R.id.description_txt)
    TextView descriptionTxt;

    private static final String TAG = "AboutActivity";
    SessionManager sessionManager;
    HashMap<String, String> userDetails = new HashMap<>();

    //Menu
    TextView textCartItemCount;
    //    GEOFIRE
    DatabaseReference mDatabaseReference;
    private static final String GEO_FIRE_DB = CONST.FIREBASE_URL;
    FirebaseDatabase mFirebaseDatabase;
    Firebase reference1;
    @BindView(R.id.parent_layout)
    ScrollView parentLayout;
    private APIInterface apiInterface;
    private String userId;

    private String descriptionStr;
    Spanned htmlAsSpanned;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);


        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        userDetails = sessionManager.getUserDetails();

        userId = userDetails.get(KEY_USER_ID);

        descriptionStr = getIntent().getStringExtra(CONST.Params.DESC_TYPE);

        actionBar();

        jsonGetDescription(descriptionStr);
    }

    private void jsonGetDescription(String description) {

        CommonFunctions.showProgressDialog(this);

        if (CommonFunctions.isGPSEnabled(this)) {
            Call<DescriptionPojo> call = apiInterface.getDescription(description, sessionManager.getHeader());
            call.enqueue(new Callback<DescriptionPojo>() {
                @Override
                public void onResponse(Call<DescriptionPojo> call, Response<DescriptionPojo> response) {

                    CommonFunctions.removeProgressDialog();

                    if (response.code() == 200) {

                        if (response.body() != null) {
                            if (response.body().getStatus()) {

                                switch (description) {
                                    case "get-privacy":
                                        htmlAsSpanned = Html.fromHtml(response.body().getPrivacy());
                                        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F3807A'>Privacy Policy </font>"));
                                        break;
                                    case "get-aboutus":
                                        htmlAsSpanned = Html.fromHtml(response.body().getAboutUs());
                                        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F3807A'>About Us </font>"));
                                        break;
                                    default:
                                        htmlAsSpanned = Html.fromHtml(response.body().getSettings());
                                        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F3807A'>Contact Us </font>"));
                                        break;
                                }

                                descriptionTxt.setText(htmlAsSpanned);

                            } else
                                CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());
                        }

                    } else
                        CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, getApplicationContext());
                }

                @Override
                public void onFailure(Call<DescriptionPojo> call, Throwable t) {
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
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#F3807A'> </font>"));
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
                intent = new Intent(AboutActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_cart:
                intent = new Intent(AboutActivity.this, CartActivity.class);
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
}
