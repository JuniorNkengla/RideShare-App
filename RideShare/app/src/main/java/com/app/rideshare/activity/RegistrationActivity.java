package com.app.rideshare.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.rideshare.R;
import com.app.rideshare.api.ApiServiceModule;
import com.app.rideshare.api.RestApiInterface;
import com.app.rideshare.api.response.SendOTPResponse;
import com.app.rideshare.api.response.SignupResponse;
import com.app.rideshare.notification.GCMRegistrationIntentService;
import com.app.rideshare.utils.AppUtils;
import com.app.rideshare.utils.MessageUtils;
import com.app.rideshare.utils.PrefUtils;
import com.app.rideshare.view.CustomProgressDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationActivity extends AppCompatActivity {
    CustomProgressDialog mProgressDialog;
    String token;
    private EditText mFirstNameEt;
    private EditText mLastNameEt;
    private EditText mEmailEt;
    private EditText mMobileEt;
    private EditText mPasswordEt;
    private EditText mConfirmPasswordEt;
    private TextView mSignupTv;
    //private Typeface mRobotoMedium;
    private TextView mAuthenticationTv;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Sign Up");

        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                Typeface titleFont = Typeface.
                        createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
                if (tv.getText().equals(toolbar.getTitle())) {
                    tv.setTypeface(titleFont);
                    break;
                }
            }
        }
        PrefUtils.initPreference(this);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {


            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    token = intent.getStringExtra("token");
                    Log.d("token", token);
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                } else {
                }
            }
        };

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (ConnectionResult.SUCCESS != resultCode) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
            }
        } else {
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

        mProgressDialog = new CustomProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //mRobotoMedium = TypefaceUtils.getTypefaceRobotoMediam(this);

        mFirstNameEt = (EditText) findViewById(R.id.first_name_et);
        mLastNameEt = (EditText) findViewById(R.id.last_name_et);
        mEmailEt = (EditText) findViewById(R.id.email_et);
        mMobileEt = (EditText) findViewById(R.id.mobile_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mConfirmPasswordEt = (EditText) findViewById(R.id.confirm_password_et);

        mSignupTv = (TextView) findViewById(R.id.signup_tv);

        /*mSignupTv.setTypeface(mRobotoMedium);
        mFirstNameEt.setTypeface(mRobotoMedium);
        mLastNameEt.setTypeface(mRobotoMedium);
        mEmailEt.setTypeface(mRobotoMedium);
        mMobileEt.setTypeface(mRobotoMedium);
        mPasswordEt.setTypeface(mRobotoMedium);
        mConfirmPasswordEt.setTypeface(mRobotoMedium);*/

        mAuthenticationTv = (TextView) findViewById(R.id.authentication_sent_tv);
        //mAuthenticationTv.setTypeface(mRobotoMedium);

        mSignupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFirstNameEt.getText().toString().isEmpty()) {
                    MessageUtils.showFailureMessage(RegistrationActivity.this, "Please enter First Name.");
                } else if (mLastNameEt.getText().toString().isEmpty()) {
                    MessageUtils.showFailureMessage(RegistrationActivity.this, "Please enter Last Name.");
                } else if (mEmailEt.getText().toString().isEmpty()) {
                    MessageUtils.showFailureMessage(RegistrationActivity.this, "Please enter Email.");
                } else if (mMobileEt.getText().toString().isEmpty()) {
                    MessageUtils.showFailureMessage(RegistrationActivity.this, "Please enter Mobile Number.");
                } else if (mPasswordEt.getText().toString().isEmpty() || mPasswordEt.getText().toString().length() < 8) {
                    MessageUtils.showFailureMessage(RegistrationActivity.this, "Please enter valid Password.");
                } else if (mConfirmPasswordEt.getText().toString().isEmpty() || mConfirmPasswordEt.getText().toString().length() < 8) {
                    MessageUtils.showFailureMessage(RegistrationActivity.this, "Please enter valid Password.");
                } else if (!mConfirmPasswordEt.getText().toString().equals(mPasswordEt.getText().toString())) {
                    MessageUtils.showFailureMessage(RegistrationActivity.this, "Password and Confirm password must be Same.");
                } else if (!AppUtils.isEmail(mEmailEt.getText().toString())) {
                    MessageUtils.showFailureMessage(RegistrationActivity.this, "Please enter valid email.");
                } else {
                    registerUser(mFirstNameEt.getText().toString(), mLastNameEt.getText().toString(), mEmailEt.getText().toString(), mMobileEt.getText().toString(), mPasswordEt.getText().toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void registerUser(String mFirstName, String mLastName, String mEmail, String mMobile, String password) {

        mProgressDialog.show();
        ApiServiceModule.createService(RestApiInterface.class).signup(mFirstName, mLastName, mEmail, mMobile, password, token).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                mProgressDialog.cancel();
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().getmStatus().equals("error")) {
                        PrefUtils.putBoolean("isFriends", true);
                        PrefUtils.addUserInfo(response.body().getMlist().get(0));
                        PrefUtils.putBoolean("islogin", true);
                        sendOTP(response.body().getMlist().get(0).getmMobileNo(), response.body().getMlist().get(0).getmUserId());
                    } else {
                        MessageUtils.showSuccessMessage(RegistrationActivity.this, response.body().getmMessage());
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                t.printStackTrace();
                Log.d("error", t.toString());
                mProgressDialog.cancel();
            }
        });
    }

    private void sendOTP(final String mobileNuber, String nUserId) {
        mProgressDialog.show();
        ApiServiceModule.createService(RestApiInterface.class).sendOTP(mobileNuber, nUserId).enqueue(new Callback<SendOTPResponse>() {
            @Override
            public void onResponse(Call<SendOTPResponse> call, Response<SendOTPResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent i = new Intent(RegistrationActivity.this, VerifyMobileNumberActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    MessageUtils.showPleaseTryAgain(context);
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }
}
