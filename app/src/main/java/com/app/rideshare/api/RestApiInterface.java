package com.app.rideshare.api;

import com.app.rideshare.api.request.ContactRequest;
import com.app.rideshare.api.response.AcceptRequest;
import com.app.rideshare.api.response.CancelRequest;
import com.app.rideshare.api.response.ContactResponse;
import com.app.rideshare.api.response.HistoryResponse;
import com.app.rideshare.api.response.RideSelect;
import com.app.rideshare.api.response.SendOTPResponse;
import com.app.rideshare.api.response.SendResponse;
import com.app.rideshare.api.response.SignupResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestApiInterface {

    @FormUrlEncoded
    @POST("auth/register")
    Call<SignupResponse> signup(@Field("u_firstname") String mFirstName, @Field("u_lastname") String mLastName, @Field("u_email") String mEmail, @Field("u_mo_number") String mMobileNuber, @Field("u_password") String mPassword,@Field("deviceToken") String deviceTocken);


    @FormUrlEncoded
    @POST("auth/login")
    Call<SignupResponse> login(@Field("u_email") String mEmail, @Field("u_password") String mPassword,@Field("deviceToken") String deviceTocken);

    @FormUrlEncoded
    @POST("auth/select_ride")
    Call<RideSelect> selectRide(@Field("u_id") String mId, @Field("u_ride_type") String mType, @Field("u_lat") String mLatitude, @Field("u_long") String mLongitude);


    @FormUrlEncoded
    @POST("user/getUserContacts")
    Call<RideSelect> getUser(@Field("user_id") String mId, @Field("friend_list_type") String mType, @Field("u_lat") String mLatitude, @Field("u_long") String mLongitude,@Field("u_ride_type") String mRideType);

    @FormUrlEncoded
    @POST("auth/facebook_register")
    Call<SignupResponse> signfacebook(@Field("facebook_id") String mFacebookId, @Field("u_email") String mEmail, @Field("u_firstname") String mFirstName, @Field("u_lastname") String mLastName,@Field("deviceToken") String deviceTocken);


    @FormUrlEncoded
    @POST("auth/google_register")
    Call<SignupResponse> signGoogleplus(@Field("google_id") String mGoogleId, @Field("u_email") String mEmail, @Field("u_firstname") String mFirstName, @Field("u_lastname") String mLastName,@Field("deviceToken") String deviceTocken);


    @FormUrlEncoded
    @POST("user/sendTextMessage")
    Call<SendOTPResponse> sendOTP(@Field("mobile_number") String mMobileNuber, @Field("user_id") String mUserId);


    @FormUrlEncoded
    @POST("user/verifyMobile")
    Call<SendOTPResponse> verifyOTP(@Field("user_id") String mUserId, @Field("token_number") String otp);


    @POST("user/addUserContacts")
    Call<ContactResponse> syncContact(@Body ContactRequest user);

    @FormUrlEncoded
    @POST("user/sendRequest")
    Call<SendResponse> sendRequest(@Field("user_id") String mUserId,@Field("from_id") String mFromUserId,
                                   @Field("start_latitude") String startlatitude,@Field("start_longitude") String startLongitude,
                                   @Field("end_latitude") String endlatitude,@Field("end_longitude") String endlongitude,
                                   @Field("u_ride_type") String mtype,@Field("start_address") String mStartAddress,@Field("end_address") String mEndAddress);

    @FormUrlEncoded
    @POST("user/acceptDeclineRequest")
    Call<AcceptRequest> acceptRequest(@Field("ride_id") String mRideId, @Field("request_status") String mRequestStatus);


    @FormUrlEncoded
    @POST("user/cancelRide")
    Call<CancelRequest> cancelRequest(@Field("ride_id") String mRideId);

    @FormUrlEncoded
    @POST("user/getRideHistory")
    Call<HistoryResponse> getHistory(@Field("user_id") String mUserId);
}
