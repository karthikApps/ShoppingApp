package com.android.shoppingapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.shoppingapp.Adapter.LatestCollectionAdapter;
import com.android.shoppingapp.Adapter.SlideAdapter;
import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.LatestCollectionPojo;
import com.android.shoppingapp.Model.SliderPojo;
import com.android.ecommerce.R;
import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.shoppingapp.Global.SessionManager.KEY_USER_EMAIL;
import static com.android.shoppingapp.Global.SessionManager.KEY_USER_ID;
import static com.android.shoppingapp.Global.SessionManager.KEY_USER_IMAGE;
import static com.android.shoppingapp.Global.SessionManager.KEY_USER_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    SessionManager sessionManager;
    HashMap<String, String> userDetails = new HashMap<>();

    //Menu
    TextView textCartItemCount;
    //    GEOFIRE
    DatabaseReference mDatabaseReference;
    private static final String GEO_FIRE_DB = CONST.FIREBASE_URL;
    FirebaseDatabase mFirebaseDatabase;
    Firebase reference1;

    String userId;
    APIInterface apiInterface;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.latest_more_txt)
    TextView latestMoreTxt;
    @BindView(R.id.latest_collection_rv)
    RecyclerView latestCollectionRv;
    @BindView(R.id.we_choose_more_txt)
    TextView weChooseMoreTxt;
    @BindView(R.id.we_choose_rv)
    RecyclerView weChooseRv;
    @BindView(R.id.parent_layout)
    CoordinatorLayout parentLayout;

    private ArrayList<String> XMENArray = new ArrayList<>();

    private ArrayList<LatestCollectionPojo.Data> latestList = new ArrayList<>();
    private ArrayList<LatestCollectionPojo.Data> chooseList = new ArrayList<>();


    private int currentPage = 0;
    private LinearLayoutManager MyLayoutManager;
    private LinearLayoutManager MyLayoutManager2;
    private LatestCollectionAdapter latestAdapter;
    private LatestCollectionAdapter chooseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.v_lit_grey));
        }

        sessionManager = new SessionManager(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        userDetails = sessionManager.getUserDetails();

        userId = userDetails.get(KEY_USER_ID);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
        toggle.getDrawerArrowDrawable().setGapSize(10f);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.nav_header_user_name);
        TextView nav_email = (TextView) hView.findViewById(R.id.nav_header_user_email);
        ImageView nav_user_img = (ImageView) hView.findViewById(R.id.nav_header_user_image);
        nav_user.setText(userDetails.get(KEY_USER_NAME));
        nav_email.setText(userDetails.get(KEY_USER_EMAIL));

        Glide.with(getApplicationContext())
                .load(userDetails.get(KEY_USER_IMAGE))
                .into(nav_user_img);

        MyLayoutManager = new LinearLayoutManager(getApplicationContext());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        latestCollectionRv.setLayoutManager(MyLayoutManager);
        latestAdapter = new LatestCollectionAdapter(this, latestList);
        latestCollectionRv.setAdapter(latestAdapter);

        MyLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        weChooseRv.setLayoutManager(MyLayoutManager2);
        chooseAdapter = new LatestCollectionAdapter(this, chooseList);
        weChooseRv.setAdapter(chooseAdapter);

        jsonSlider();

        //setFirebaseValue();

        getFirebaseValue();


        jsonLatest("1");
        jsonLatest("2");


    }

    private void setFirebaseValue() {


        Firebase.setAndroidContext(this);
        reference1 = new Firebase(GEO_FIRE_DB + userId);//u have to give ur dynamic value (Booking Id)

        Map<String, String> map = new HashMap<String, String>();
        map.put(CONST.Params.CartValue, "16");//UserType
        reference1.push().setValue(map);

    }

    public void jsonSlider() {

        CommonFunctions.showProgressDialog(this);

        if (CommonFunctions.isGPSEnabled(this)) {
            Call<SliderPojo> call = apiInterface.getSlider();
            call.enqueue(new Callback<SliderPojo>() {
                @Override
                public void onResponse(Call<SliderPojo> call, Response<SliderPojo> response) {

                    CommonFunctions.removeProgressDialog();

                    if (response.code() == 200) {

                        if (response.body() != null) {
                            if (response.body().getStatus()) {

                                List<SliderPojo.Data> list = response.body().getData();

                                for (int i = 0; i < list.size(); i++) {
                                    XMENArray.add(list.get(i).getImage());
                                }
                                init();
                            } else
                                CommonFunctions.shortToast(parentLayout, "Error", Snackbar.LENGTH_SHORT, getApplicationContext());
                        }

                    } else
                        CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, getApplicationContext());
                }

                @Override
                public void onFailure(Call<SliderPojo> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    CommonFunctions.removeProgressDialog();
                }
            });
        } else
            CommonFunctions.shortToast(parentLayout, "Network not found", Snackbar.LENGTH_SHORT, getApplicationContext());

    }

    public void jsonLatest(String type) {


        if (CommonFunctions.isGPSEnabled(this)) {
            Call<LatestCollectionPojo> call = apiInterface.getCollection(sessionManager.getHeader(), type);
            call.enqueue(new Callback<LatestCollectionPojo>() {
                @Override
                public void onResponse(Call<LatestCollectionPojo> call, Response<LatestCollectionPojo> response) {

                    CommonFunctions.removeProgressDialog();

                    if (response.code() == 200) {

                        if (response.body() != null) {
                            if (response.body().getStatus()) {

                                if (type.equalsIgnoreCase("1")) {
                                    latestList.clear();
                                    latestList.addAll(response.body().getData());
                                    latestAdapter.notifyDataChanged();
                                } else if (type.equalsIgnoreCase("2")) {
                                    chooseList.clear();
                                    chooseList.addAll(response.body().getData());
                                    chooseAdapter.notifyDataChanged();
                                }


                            } else
                                CommonFunctions.shortToast(parentLayout, "Error", Snackbar.LENGTH_SHORT, getApplicationContext());
                        }

                    } else
                        CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, getApplicationContext());
                }

                @Override
                public void onFailure(Call<LatestCollectionPojo> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    CommonFunctions.removeProgressDialog();
                }
            });
        }

    }

    private void init() {


        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SlideAdapter(MainActivity.this, XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMENArray.size()) {
                    currentPage = 0;
                }
                pager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 6000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {

            Intent intent = new Intent(MainActivity.this, CartActivity.class);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        switch (id) {

            case R.id.nav_my_account:
                intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra(CONST.Params.PROFILE, "1");
                startActivity(intent);
                break;
            case R.id.nav_product_filter:
                intent = new Intent(MainActivity.this, ProductListActivity.class);
                intent.putExtra(CONST.Params.TYPE, "0");
                startActivity(intent);
                break;
            case R.id.nav_wish_list:
                intent = new Intent(MainActivity.this, WishListActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_cart:
                intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_gift:
                intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_category:
                intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                intent.putExtra(CONST.Params.DESC_TYPE,"get-settings");
                startActivity(intent);
                break;
            case R.id.nav_privacy_policy:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                intent.putExtra(CONST.Params.DESC_TYPE,"get-privacy");
                startActivity(intent);
                break;
            case R.id.nav_about_us:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                intent.putExtra(CONST.Params.DESC_TYPE,"get-aboutus");
                startActivity(intent);
                break;
            case R.id.nav_logout:
                sessionManager.logoutUser();
                break;


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick({R.id.latest_more_txt, R.id.we_choose_more_txt})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.latest_more_txt:
                intent = new Intent(MainActivity.this, ProductListActivity.class);
                intent.putExtra(CONST.Params.TYPE, "1");
                startActivity(intent);
                break;
            case R.id.we_choose_more_txt:
                intent = new Intent(MainActivity.this, ProductListActivity.class);
                intent.putExtra(CONST.Params.TYPE, "2");
                startActivity(intent);
                break;
        }
    }
}
