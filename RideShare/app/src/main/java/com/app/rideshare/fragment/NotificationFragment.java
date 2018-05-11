package com.app.rideshare.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.rideshare.R;
import com.app.rideshare.api.RideShareApi;
import com.app.rideshare.model.GroupList;
import com.app.rideshare.model.NotificationList;
import com.app.rideshare.utils.PrefUtils;
import com.app.rideshare.view.CustomProgressDialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private ArrayList<NotificationList> mListNotification = new ArrayList<>();
    private NotificationAdapter adapterNotification;
    private ListView mLvNotification;

    private TextView mNoUserTv;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notificarion, container,
                false);

        mLvNotification = rootView.findViewById(R.id.mLvGroup);

        mNoUserTv = rootView.findViewById(R.id.no_user);
        mNoUserTv.setVisibility(View.GONE);

        new AsyncNotification().execute();

        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncNotification extends AsyncTask<Object, Integer, Object> {

        private CustomProgressDialog mProgressDialog;

        AsyncNotification() {
            mProgressDialog = new CustomProgressDialog(getActivity());
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        public Object doInBackground(Object... params) {
            try {
                return RideShareApi.groupJoinRequestList(PrefUtils.getUserInfo().getmUserId());
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public void onPostExecute(Object result) {
            super.onPostExecute(result);

            mProgressDialog.dismiss();

            try {

                JSONObject jsonObject = new JSONObject(result.toString());

                if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                    JSONArray jArrayResult = new JSONArray(jsonObject.getString("message"));

                    for (int i = 0; i < jArrayResult.length(); i++) {

                        JSONObject jObjResult = jArrayResult.getJSONObject(i);

                        NotificationList bean = new NotificationList();

                        bean.setU_id(jObjResult.getString("u_id"));
                        bean.setU_firstname(jObjResult.getString("u_firstname"));
                        bean.setU_lastname(jObjResult.getString("u_lastname"));
                        bean.setU_email(jObjResult.getString("u_email"));
                        bean.setProfile_image(jObjResult.getString("profile_image"));

                        bean.setCategory_id(jObjResult.getString("category_id"));
                        bean.setCategory_name(jObjResult.getString("category_name"));

                        bean.setGroup_id(jObjResult.getString("group_id"));
                        bean.setGroup_name(jObjResult.getString("group_name"));

                        bean.setStatus(jObjResult.getString("status"));

                        mListNotification.add(bean);
                    }

                    if (mListNotification.size() == 0)
                        mNoUserTv.setVisibility(View.VISIBLE);

                    adapterNotification = new NotificationAdapter();
                    mLvNotification.setAdapter(adapterNotification);

                } else {
                    mNoUserTv.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class NotificationAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        private NotificationAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mListNotification.size();
        }

        @Override
        public GroupList getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            private CircularImageView circularImageView;
            private TextView mUserName;
            private TextView mGroupName;
            private ImageView imgDecline;
            private ImageView imgAccept;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(final int pos, View vi, ViewGroup parent) {

            final ViewHolder holder;

            if (vi == null) {

                vi = mInflater.inflate(R.layout.item_group_user_request, null);

                holder = new ViewHolder();

                holder.circularImageView = vi.findViewById(R.id.circularImageView);
                holder.mUserName = vi.findViewById(R.id.mUserName);
                holder.mGroupName = vi.findViewById(R.id.mGroupName);

                holder.imgDecline = vi.findViewById(R.id.imgDecline);
                holder.imgAccept = vi.findViewById(R.id.imgAccept);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            final NotificationList notifBean = mListNotification.get(pos);

            holder.mUserName.setText(String.format("%s %s", notifBean.getU_firstname(), notifBean.getU_lastname()));
            holder.mGroupName.setText(notifBean.getGroup_name());

            Picasso.with(getActivity()).load(notifBean.getProfile_image()).resize(300, 300)
                    .centerCrop().error(R.drawable.user_icon).into(holder.circularImageView);

            holder.imgAccept.setTag(notifBean);
            holder.imgAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final NotificationList selNotif = (NotificationList) v.getTag();
                    new AsyncJoinGroup(selNotif.getU_id(), selNotif.getGroup_id(), "2").execute();
                    mListNotification.remove(pos);
                }
            });

            holder.imgDecline.setTag(notifBean);
            holder.imgDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final NotificationList selNotif = (NotificationList) v.getTag();
                    new AsyncJoinGroup(selNotif.getU_id(), selNotif.getGroup_id(), "3").execute();
                    mListNotification.remove(pos);
                }
            });
            return vi;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncJoinGroup extends AsyncTask<Object, Integer, Object> {

        private CustomProgressDialog mProgressDialog;

        private String user_id;
        private String group_id;
        private String status;

        AsyncJoinGroup(String user_id, String group_id, String status) {

            this.user_id = user_id;
            this.group_id = group_id;
            this.status = status;

            mProgressDialog = new CustomProgressDialog(getActivity());
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        public Object doInBackground(Object... params) {
            try {
                return RideShareApi.joinGroup(user_id, group_id, status);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public void onPostExecute(Object result) {
            super.onPostExecute(result);

            mProgressDialog.dismiss();

            try {

                JSONObject jsonObject = new JSONObject(result.toString());

                if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    adapterNotification.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}