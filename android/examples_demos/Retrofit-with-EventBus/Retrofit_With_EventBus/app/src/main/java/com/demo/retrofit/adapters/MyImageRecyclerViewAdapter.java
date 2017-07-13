package com.demo.retrofit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.retrofit.R;
import com.demo.retrofit.network.response.submodel.ImageResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ImageResult}
 */
public class MyImageRecyclerViewAdapter extends
        RecyclerView.Adapter<MyImageRecyclerViewAdapter.ViewHolder> {

    private List<ImageResult> mValues;

    public MyImageRecyclerViewAdapter(List<ImageResult> items) {
        mValues = items;
    }

    public void updateData(List<ImageResult> items) {
        mValues = items;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (holder.txtID != null) {
            holder.txtID.setText(String.valueOf(mValues.get(position).getId()));
        }
        if (holder.content != null) {
            holder.content.setText(mValues.get(position).getName());
        }
        Glide.with(holder.imageView.getContext())
                .load(mValues.get(position).getImg())
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageResult mItem;

        @BindView(R.id.txtID)
        TextView txtID;

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.imageView)
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
