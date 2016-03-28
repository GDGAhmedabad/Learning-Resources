package com.admobdemo.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class AdapterListView extends BaseAdapter {
    private ArrayList<Object> userList;
    private Context mContext;
    private static final int ITEM_VIEW_TYPE_ITEM = 0;
    private static final int ITEM_VIEW_TYPE_AD_MOB = 1;
    private static final int ITEM_VIEW_TYPE_COUNT = 2;

    public AdapterListView(Context context, ArrayList<Object> userList) {
        this.mContext = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int type = getItemViewType(position);

        // First, let's create a new convertView if needed. You can also
        // create a ViewHolder to speed up changes if you want ;)
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    type == ITEM_VIEW_TYPE_ITEM ? R.layout.row_user_item : R.layout.row_admob_item, parent, false);
        }

        // We can now fill the list item view with the appropriate data.
        if (type == ITEM_VIEW_TYPE_AD_MOB) {
            AdView mAdView = (AdView) convertView.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("BEB5489770BFAEF37453BB5474C6FA2A").build();
            mAdView.loadAd(adRequest);
        } else {
            final ModelUser user = (ModelUser) getItem(position);
            TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            TextView tvUserEmail = (TextView) convertView.findViewById(R.id.tvUserEmail);
            TextView tvUserPhone = (TextView) convertView.findViewById(R.id.tvUserPhone);
            tvUserName.setText(user.getUserName());
            tvUserEmail.setText(user.getUserEmail());
            tvUserPhone.setText(user.getUserPhone());
        }


        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        return (userList.get(position) instanceof ModelUser) ? ITEM_VIEW_TYPE_ITEM : ITEM_VIEW_TYPE_AD_MOB;
    }

    @Override
    public int getViewTypeCount() {

        return ITEM_VIEW_TYPE_COUNT;
    }
}
