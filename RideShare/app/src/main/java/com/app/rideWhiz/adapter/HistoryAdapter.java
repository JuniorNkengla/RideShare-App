package com.app.rideWhiz.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.rideWhiz.R;
import com.app.rideWhiz.model.HistoryBean;
import com.app.rideWhiz.utils.DateUtils;
import com.app.rideWhiz.utils.TypefaceUtils;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private Typeface mRobotoMedium;
    private ArrayList<HistoryBean> mlist;
    private String mUserId;

    public HistoryAdapter(Context context, ArrayList<HistoryBean> mlist, String userid) {

        this.context = context;
        this.mlist = mlist;
        mUserId = userid;
        mRobotoMedium = TypefaceUtils.getTypefaceRobotoMedium(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public String getItem(int position) {
        return mlist.get(position).getFrom_id();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private TextView mNameTv;
        private TextView mRideTypeTv;
        private TextView mStatusTv;
        private TextView mStatusResultTv;
        private TextView mPickupAddressTv;
        private TextView mDropoffAddressTv;
        private TextView mTimeTv;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_history, null);
            holder = new ViewHolder();

            holder.mNameTv = (TextView) convertView.findViewById(R.id.username_tv);
            holder.mRideTypeTv = (TextView) convertView.findViewById(R.id.ride_type_tv);
            holder.mStatusTv = (TextView) convertView.findViewById(R.id.status_tv);
            holder.mStatusResultTv = (TextView) convertView.findViewById(R.id.status_result_tv);
            holder.mPickupAddressTv = (TextView) convertView.findViewById(R.id.pickup_address_tv);
            holder.mDropoffAddressTv = (TextView) convertView.findViewById(R.id.drop_off_address);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);

          /*  holder.mNameTv.setTypeface(mRobotoMedium);
            holder.mRideTypeTv.setTypeface(mRobotoMedium);
            holder.mStatusTv.setTypeface(mRobotoMedium);
            holder.mStatusResultTv.setTypeface(mRobotoMedium);
            holder.mPickupAddressTv.setTypeface(mRobotoMedium);
            holder.mDropoffAddressTv.setTypeface(mRobotoMedium);
            holder.mTimeTv.setTypeface(mRobotoMedium);*/

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        HistoryBean bean = mlist.get(position);

        if (bean.getRequest_status().equals("3")) {
            holder.mStatusResultTv.setText("Inprogress");
        } else if (bean.getRequest_status().equals("4")) {
            holder.mStatusResultTv.setText("Complete");
        } else if (bean.getRequest_status().equals("2")) {
            holder.mStatusResultTv.setText("Cancel");
        } else if (bean.getRequest_status().equals("1")) {
            holder.mStatusResultTv.setText("Accept ride");
        } else {
            holder.mStatusResultTv.setText("Decline");
        }

        if (bean.getFrom_id().equals(mUserId)) {
            holder.mNameTv.setText(bean.getToname());
        } else {
            holder.mNameTv.setText(bean.getFromname());
        }

        holder.mPickupAddressTv.setText(bean.getStarting_address());
        holder.mDropoffAddressTv.setText(bean.getEnding_address());
        holder.mTimeTv.setText(DateUtils.dateformat(bean.getTime()));

        return convertView;
    }
}