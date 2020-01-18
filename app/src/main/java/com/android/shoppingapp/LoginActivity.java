package com.android.shoppingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.shoppingapp.Global.APIClient;
import com.android.shoppingapp.Global.APIInterface;
import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.CommonFunctions;
import com.android.shoppingapp.Global.SessionManager;
import com.android.shoppingapp.Model.LoginPojo;
import com.android.ecommerce.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.login_email_edt)
    EditText loginEmailEdt;
    @BindView(R.id.login_name_edt)
    EditText loginNameEdt;
    @BindView(R.id.login_pswd_edt)
    EditText loginPswdEdt;
    @BindView(R.id.login_conf_pswd_edt)
    EditText loginConfPswdEdt;
    @BindView(R.id.login_submit_btn)
    Button loginSubmitBtn;
    @BindView(R.id.login_txt)
    TextView loginTxt;
    @BindView(R.id.sign_in_linear)
    LinearLayout signInLinear;
    @BindView(R.id.login_forgot_txt)
    TextView loginForgotTxt;

    SessionManager sessionManager;
    APIInterface apiInterface;
    @BindView(R.id.parent_layout)
    RelativeLayout parentLayout;
    private String splashStr = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sessionManager = new SessionManager(this);

        /*loginEmailEdt.setText("karthik1nedu@gmail.com");
        loginPswdEdt.setText("123456");*/

        splashStr = getIntent().getStringExtra(CONST.login);
        setVisible(splashStr);


    }

    public void jsonLogin(HashMap<String, String> map) {

        CommonFunctions.showProgressDialog(this);

        if (CommonFunctions.isGPSEnabled(this)) {
            Call<LoginPojo> call = apiInterface.loginSignup(map);
            call.enqueue(new Callback<LoginPojo>() {
                @Override
                public void onResponse(Call<LoginPojo> call, Response<LoginPojo> response) {

                    CommonFunctions.removeProgressDialog();

                    if (response.code() == 200) {

                        if (response.body() != null) {
                            if (response.body().getStatus()) {

                                LoginPojo.UserData lo = response.body().getUserData();

                                sessionManager.createLoginSession(lo.getId(), lo.getToken(), lo.getName(), lo.getEmail(), lo.getProfileImage());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else
                                CommonFunctions.shortToast(parentLayout, response.body().getMessage(), Snackbar.LENGTH_SHORT, getApplicationContext());
                        }

                    } else
                        CommonFunctions.shortToast(parentLayout, response.message(), Snackbar.LENGTH_SHORT, getApplicationContext());


                }

                @Override
                public void onFailure(Call<LoginPojo> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    CommonFunctions.removeProgressDialog();
                }
            });
        } else
            CommonFunctions.shortToast(parentLayout, "Network not found", Snackbar.LENGTH_SHORT, getApplicationContext());

    }

    private void setVisible(String login) {

        switch (login) {

            case "Register":
                loginNameEdt.setVisibility(View.VISIBLE);
                loginConfPswdEdt.setVisibility(View.VISIBLE);
                loginForgotTxt.setVisibility(View.GONE);
                loginSubmitBtn.setText("REGISTER");
                loginTxt.setText("Go to Login");
                break;
            case "Login":
                loginNameEdt.setVisibility(View.GONE);
                loginConfPswdEdt.setVisibility(View.GONE);
                loginForgotTxt.setVisibility(View.VISIBLE);
                loginSubmitBtn.setText("Start");
                loginTxt.setText("Go to Register");
                break;

        }


    }

    public void loginSubmit() {

        if (loginSubmitBtn.getText().toString().equalsIgnoreCase("Start")) {

            if (!CommonFunctions.isValidEmail(loginEmailEdt.getText().toString())) {
                loginEmailEdt.setError("Enter Valid Email");
            } else if (loginPswdEdt.getText().toString().trim().length() < 4) {
                loginPswdEdt.setError("Enter Valid Password");
            } else {
                HashMap<String, String> loginMap = new HashMap<String, String>();
                loginMap.put(CONST.Params.Email, loginEmailEdt.getText().toString());
                loginMap.put(CONST.Params.Password, loginPswdEdt.getText().toString());
                loginMap.put(CONST.Params.Type, "1");
                loginMap.put(CONST.Params.DeviceToken, sessionManager.getDeviceToken());
                jsonLogin(loginMap);
            }


        } else if (loginSubmitBtn.getText().toString().equalsIgnoreCase("REGISTER")) {

            if (!CommonFunctions.isValidEmail(loginEmailEdt.getText().toString())) {
                loginEmailEdt.setError("Enter Valid Email");
            } else if (loginPswdEdt.getText().toString().trim().length() < 4) {
                loginPswdEdt.setError("Enter Valid Password");
            } else if (loginConfPswdEdt.getText().toString().trim().length() < 4) {
                loginPswdEdt.setError("Enter Valid Password");
            } else if (!loginPswdEdt.getText().toString().trim().equals(loginConfPswdEdt.getText().toString().trim())) {
                loginPswdEdt.setError("Password Conflict");
            } else {
                HashMap<String, String> loginMap = new HashMap<String, String>();
                loginMap.put(CONST.Params.UserName, loginNameEdt.getText().toString());
                loginMap.put(CONST.Params.Email, loginEmailEdt.getText().toString());
                loginMap.put(CONST.Params.Password, loginPswdEdt.getText().toString());
                loginMap.put(CONST.Params.Type, "2");
                loginMap.put(CONST.Params.DeviceToken, sessionManager.getDeviceToken());
                jsonLogin(loginMap);
            }

        }


    }


    @OnClick({R.id.login_submit_btn, R.id.login_txt, R.id.login_forgot_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_submit_btn:
                loginSubmit();
                break;
            case R.id.login_txt:
                if (loginForgotTxt.getVisibility() == View.VISIBLE) {
                    setVisible("Register");
                } else setVisible("Login");
                break;
            case R.id.login_forgot_txt:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int which) {
                        finishAffinity();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }


}
