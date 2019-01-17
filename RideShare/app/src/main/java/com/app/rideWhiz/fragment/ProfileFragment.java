package com.app.rideWhiz.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.rideWhiz.R;
import com.app.rideWhiz.activity.EditProfileActivity;
import com.app.rideWhiz.activity.HistoryActivity;
import com.app.rideWhiz.activity.ManageCarActivity;
import com.app.rideWhiz.activity.MyGroupActivity;
import com.app.rideWhiz.activity.MyGroupSelectionActivity;
import com.app.rideWhiz.activity.RideTypeActivity;
import com.app.rideWhiz.activity.SettingActivity;
import com.app.rideWhiz.activity.SignUpActivity;
import com.app.rideWhiz.listner.OnBackPressedListener;
import com.app.rideWhiz.utils.PrefUtils;
import com.app.rideWhiz.view.CustomProgressDialog;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

public class ProfileFragment extends Fragment implements OnBackPressedListener {

    CircularImageView imgProfilePhoto;
    TextView txtUserName;

    private LinearLayout llMyProfile;
    private LinearLayout llMyGroup;
    private LinearLayout llManageCar;
    private LinearLayout llHistory;
    private LinearLayout need_ride_ll;
    private LinearLayout offer_ride_ll;
    private LinearLayout llSetting;
    private LinearLayout llLogout;
    private CustomProgressDialog mProgressDialog;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container,
                false);

        PrefUtils.initPreference(getActivity());
        //HomeActivity.setOnBackPressedListener(this);
        imgProfilePhoto = (CircularImageView) rootView.findViewById(R.id.imgProfilePhoto);

        if (PrefUtils.getUserInfo().getThumb_image().length() != 0) {
            /*Picasso.with(getActivity()).load(PrefUtils.getUserInfo().getProfile_image())
                    .resize(300, 300)
                    .centerCrop()
                    .error(R.drawable.user_icon).into(imgProfilePhoto);*/
            Glide.with(getActivity()).load(PrefUtils.getUserInfo().getThumb_image())
                    .crossFade()
                    .error(R.drawable.user_icon)
                    .into(imgProfilePhoto);
            /*mProgressDialog = new CustomProgressDialog(getActivity());
            mProgressDialog.show();
            Glide.with(getActivity())
                    .load(PrefUtils.getUserInfo().getProfile_image())
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            mProgressDialog.dismiss();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            mProgressDialog.dismiss();
                            return false;
                        }
                    })
                    .into(imgProfilePhoto);*/
        }

        txtUserName = (TextView) rootView.findViewById(R.id.txtUserName);
        txtUserName.setText(PrefUtils.getUserInfo().getmFirstName() + " " + PrefUtils.getUserInfo().getmLastName());

        llMyProfile = (LinearLayout) rootView.findViewById(R.id.llMyProfile);
        llMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                getActivity().finish();
            }
        });

        llMyGroup = (LinearLayout) rootView.findViewById(R.id.llMyGroup);
        llMyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MyGroupActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                getActivity().finish();
            }
        });

        offer_ride_ll = (LinearLayout) rootView.findViewById(R.id.offer_ride_ll);
        offer_ride_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyGroupSelectionActivity.class);
                startActivity(intent);
            }
        });

        llManageCar = (LinearLayout) rootView.findViewById(R.id.llManageCar);
        llManageCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ManageCarActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                getActivity().finish();
            }
        });

        need_ride_ll = (LinearLayout) rootView.findViewById(R.id.need_ride_ll);
        need_ride_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RideTypeActivity.class);
                startActivity(intent);
            }
        });

        llHistory = (LinearLayout) rootView.findViewById(R.id.llHistory);
        llHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HistoryActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                getActivity().finish();
            }
        });

        llSetting = (LinearLayout) rootView.findViewById(R.id.llSetting);
        llSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });




        llLogout = (LinearLayout) rootView.findViewById(R.id.llLogout);
        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(getResources().getString(R.string.logout))
                        .setMessage(getResources().getString(R.string.logout_message))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    PrefUtils.putBoolean("islogin", false);

                                    PrefUtils.putString("loginwith", "");

                                    Intent intent = new Intent(getActivity(),
                                            SignUpActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    getActivity().finish();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        getActivity().finishAffinity();
                                    }

                                } catch (Exception e) {
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();

            }
        });
        return rootView;
    }

    @Override
    public void doBack() {
        getActivity().finish();
    }
}