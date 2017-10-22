package com.rocky.rxretrofit.ui.adapter;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocky.rxretrofit.R;
import com.rocky.rxretrofit.support.retrofit.model.ContactInfoModel;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;

import java.util.ArrayList;

public class ContactInfoAdapter extends RecyclerView.Adapter<ContactInfoAdapter.DataObjectHolder> implements FlexibleDividerDecoration.ColorProvider {
    private static MyClickListener myClickListener;
    private Activity activity;
    private ArrayList<ContactInfoModel> contactInfoModels;
    @ColorInt
    private int listDivider;

    public ContactInfoAdapter(Activity activity, ArrayList<ContactInfoModel> contactInfoModels) {
        this.activity = activity;
        this.contactInfoModels = contactInfoModels;
        listDivider = ContextCompat.getColor(activity, R.color.colorAccent);
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        ContactInfoAdapter.myClickListener = myClickListener;
    }

    @Override
    public int getItemCount() {
        return getContactInfoModels().size();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        ContactInfoModel contactInfoModel = getItem(position);
        if (contactInfoModel != null) {
            holder.txtNameVal.setText(contactInfoModel.getName());
            holder.txtEmailVal.setText(contactInfoModel.getEmail());
            ContactInfoModel.PhoneBean phone = contactInfoModel.getPhone();
            holder.txtPhoneHomeVal.setText(phone.getHome());
            holder.txtPhoneMobileVal.setText(phone.getMobile());
        }
    }

    public void addAll(ArrayList<ContactInfoModel> contactInfoModels) {
        int position = getItemCount() - 1;
        this.getContactInfoModels().addAll(contactInfoModels);
        notifyItemRangeInserted(position, contactInfoModels.size());
    }

    public void addItem(ContactInfoModel contactInfoModel, int index) {
        getContactInfoModels().add(contactInfoModel);
        notifyItemInserted(index);
    }

    public void addItem(ContactInfoModel contactInfoModel) {
        getContactInfoModels().add(contactInfoModel);
        notifyItemInserted(getItemCount() - 1);
    }

    private ArrayList<ContactInfoModel> getContactInfoModels() {
        return contactInfoModels;
    }

    public void deleteAll() {
        getContactInfoModels().clear();
        notifyDataSetChanged();
    }

    public ContactInfoModel getItem(int index) {
        return getContactInfoModels().get(index);
    }

    @Override
    public int dividerColor(int position, RecyclerView parent) {
        return listDivider;
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private AppCompatTextView txtNameVal;
        private AppCompatTextView txtEmailVal;
        private AppCompatTextView txtPhoneHomeVal;
        private AppCompatTextView txtPhoneMobileVal;

        DataObjectHolder(View itemView) {
            super(itemView);
            txtNameVal = (AppCompatTextView) itemView.findViewById(R.id.txt_name_val);
            txtEmailVal = (AppCompatTextView) itemView.findViewById(R.id.txt_email_val);
            txtPhoneHomeVal = (AppCompatTextView) itemView.findViewById(R.id.txt_phone_home_val);
            txtPhoneMobileVal = (AppCompatTextView) itemView.findViewById(R.id.txt_phone_mobile_val);
        }

        @Override
        public void onClick(View v) {
            if (myClickListener != null) myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }
}
