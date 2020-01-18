package com.android.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.ecommerce.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentSuccessActivity extends AppCompatActivity {

    @BindView(R.id.succ_img)
    ImageView succImg;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.succ_img)
    public void onViewClicked() {
        Intent intent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
