package com.app.rideshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.rideshare.R;
import com.app.rideshare.api.ApiServiceModule;
import com.app.rideshare.api.RestApiInterface;
import com.app.rideshare.api.response.SendOTPResponse;
import com.app.rideshare.model.User;
import com.app.rideshare.utils.PrefUtils;
import com.app.rideshare.utils.ToastUtils;
import com.app.rideshare.view.CustomProgressDialog;
import com.dpizarro.pinview.library.PinView;
import com.dpizarro.pinview.library.PinViewSettings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyMobileNumberActivity extends AppCompatActivity {

    CustomProgressDialog mProgressDialog;
    private TextView mResendTv;
    private TextView mVerifyTv;
    User mBean;
    private String mOTP="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_mobile_number);

        mProgressDialog = new CustomProgressDialog(this);


        PrefUtils.initPreference(this);
        mBean = PrefUtils.getUserInfo();

        PinView pinView = (PinView) findViewById(R.id.pinView);

        PinViewSettings pinViewSettings = new PinViewSettings.Builder()
                .withMaskPassword(false)
                .withDeleteOnClick(false)
                .withKeyboardMandatory(false)
                .withNumberPinBoxes(5)
                .withNativePinBox(false)
                .build();

        pinView.setSettings(pinViewSettings);

        mResendTv = (TextView) findViewById(R.id.resend_tv);
        mVerifyTv = (TextView) findViewById(R.id.verify_tv);

        mResendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP(mBean.getmMobileNo(), mBean.getmUserId());
            }
        });

        pinView.setOnCompleteListener(new PinView.OnCompleteListener() {
            @Override
            public void onComplete(boolean completed, final String pinResults) {
                if (completed)
                {
                    mOTP=pinResults;
                }
            }
        });

        mVerifyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(mOTP.length()==5)
                {
                    VerifyOTP(mOTP,mBean.getmUserId());
                }

            }
        });

    }

    private void sendOTP(final String mobileNuber, String nUserId) {
        mProgressDialog.show();
        ApiServiceModule.createService(RestApiInterface.class).sendOTP(mobileNuber, nUserId).enqueue(new Callback<SendOTPResponse>() {
            @Override
            public void onResponse(Call<SendOTPResponse> call, Response<SendOTPResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ToastUtils.showShort(VerifyMobileNumberActivity.this, "OTP Sent");
                } else {
                    ToastUtils.showShort(VerifyMobileNumberActivity.this, "Please try againg..");
                }
                mProgressDialog.cancel();
            }

            @Override
            public void onFailure(Call<SendOTPResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d("error", t.toString());
                mProgressDialog.cancel();
            }
        });
    }

    private void VerifyOTP(String otp, String nUserId) {
        mProgressDialog.show();
        ApiServiceModule.createService(RestApiInterface.class).verifyOTP(nUserId, otp).enqueue(new Callback<SendOTPResponse>() {
            @Override
            public void onResponse(Call<SendOTPResponse> call, Response<SendOTPResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    if(response.body().getmStatus().equals("success"))
                    {
                        mBean.setmIsVerify("1");
                        PrefUtils.addUserInfo(mBean);
                        Intent i = new Intent(getBaseContext(), RideTypeActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        ToastUtils.showShort(VerifyMobileNumberActivity.this, "Please try againg..");
                    }
                } else {
                    ToastUtils.showShort(VerifyMobileNumberActivity.this, "Please try againg..");
                }
                mProgressDialog.cancel();
            }

            @Override
            public void onFailure(Call<SendOTPResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d("error", t.toString());
                mProgressDialog.cancel();
            }
        });
    }
}
