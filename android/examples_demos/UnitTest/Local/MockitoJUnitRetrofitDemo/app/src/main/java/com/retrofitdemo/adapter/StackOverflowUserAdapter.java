package com.retrofitdemo.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.retrofitdemo.app.R;
import com.retrofitdemo.api.response.StackOverflowUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StackOverflowUserAdapter extends RecyclerView.Adapter<StackOverflowUserAdapter.RecyclerViewHolders> {

    private List<StackOverflowUser.Item> users;
    private Context mContext;

    private CustomTabsClient mClient;

    public StackOverflowUserAdapter(Context content, List<StackOverflowUser.Item> users) {
        this.mContext = content;
        this.users = users;

        CustomTabsServiceConnection mConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mClient = customTabsClient;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mClient = null;
            }
        };

        String packageName = "com.android.chrome";
        CustomTabsClient.bindCustomTabsService(content, packageName, mConnection);
    }

    @Override
    public StackOverflowUserAdapter.RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_result, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(mContext, layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(StackOverflowUserAdapter.RecyclerViewHolders holder, int position) {

        StackOverflowUser.Item userObj = users.get(position);
        StackOverflowUser.BadgeCounts userBadge = userObj.getBadgeCounts();

        holder.tvPerson_name.setText(userObj.getDisplayName());
        holder.tvReputation.setText(userObj.getReputation() + "");
        holder.tvGoldBadge.setText(userBadge.getGold() + "");
        holder.tvSilverBadge.setText(userBadge.getSilver() + "");
        holder.tvBronzeBadge.setText(userBadge.getBronze() + "");

        Picasso.with(mContext).load(userObj.getProfileImage())
                .error(R.drawable.profile_place_holder)
                .placeholder(R.drawable.profile_place_holder)
                .into(holder.personPhoto);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;

        TextView tvPerson_name, tvReputation, tvGoldBadge, tvSilverBadge, tvBronzeBadge;
        ImageView personPhoto;

        public RecyclerViewHolders(Context mContext, View layoutView) {
            super(layoutView);
            layoutView.setOnClickListener(this);
            tvPerson_name = (TextView) itemView.findViewById(R.id.tvPerson_name);
            tvReputation = (TextView) itemView.findViewById(R.id.tvReputation);
            personPhoto = (ImageView) itemView.findViewById(R.id.circleView);

            tvGoldBadge = (TextView) itemView.findViewById(R.id.tvGoldBadge);
            tvSilverBadge = (TextView) itemView.findViewById(R.id.tvSilverBadge);
            tvBronzeBadge = (TextView) itemView.findViewById(R.id.tvBronzeBadge);
        }

        @Override
        public void onClick(View v) {
            loadCustomTabs(users.get(getAdapterPosition()).getLink());
        }
    }

    public void loadCustomTabs(String url) {
        CustomTabsIntent.Builder mBuilder = new CustomTabsIntent.Builder(getSession());
        mBuilder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        mBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.ic_arrow_back_white_24dp));
        mBuilder.addMenuItem("Share", setMenuItem(url));
        mBuilder.setActionButton(BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.ic_file_download_white_24dp), "Engineering @Lets Nurture", addActionButton());
        mBuilder.setStartAnimations(mContext, R.anim.slide_in_right, R.anim.slide_out_left);
        mBuilder.setExitAnimations(mContext, R.anim.slide_in_left, R.anim.slide_out_right);
        CustomTabsIntent mIntent = mBuilder.build();
        mIntent.launchUrl((Activity) mContext, Uri.parse(url));
    }

    private CustomTabsSession getSession() {
        return mClient.newSession(new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
            }
        });
    }

    private PendingIntent setMenuItem(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return PendingIntent.getActivity(mContext, 0, shareIntent, 0);
    }

    private PendingIntent addActionButton() {
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://engineering.letsnurture.com/"));
        return PendingIntent.getActivity(mContext, 0, playStoreIntent, 0);
    }
}
