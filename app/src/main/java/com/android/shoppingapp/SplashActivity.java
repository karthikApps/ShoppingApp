package com.android.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.shoppingapp.Global.CONST;
import com.android.shoppingapp.Global.SessionManager;
import com.android.ecommerce.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.splash_img)
    ImageView splashImg;
    @BindView(R.id.progressBar)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.loading_linear)
    LinearLayout loadingLinear;
    @BindView(R.id.splash_login_btn)
    Button splashLoginBtn;
    @BindView(R.id.splash_register_btn)
    Button splashRegisterBtn;
    @BindView(R.id.login_linear)
    LinearLayout loginLinear;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                if (!sessionManager.isLoggedIn()) {
                    loadingLinear.setVisibility(View.GONE);
                    loginLinear.setVisibility(View.VISIBLE);
                }else{
                   Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                   startActivity(intent);
                    // close this activity
                    finish();
                }

            }
        }, 3 * 1000); // wait for 2 seconds

    }

    @OnClick({R.id.splash_login_btn, R.id.splash_register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.splash_login_btn:
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                intent.putExtra(CONST.login,"Login");
                startActivity(intent);
                break;
            case R.id.splash_register_btn:
                Intent intents = new Intent(SplashActivity.this,LoginActivity.class);
                intents.putExtra(CONST.login,"Register");
                startActivity(intents);
                break;
        }
    }
}
