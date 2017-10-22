package com.rocky.rxretrofit.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;

import com.rocky.rxretrofit.R;
import com.rocky.rxretrofit.support.retrofit.model.ContactInfoModel;
import com.rocky.rxretrofit.support.retrofit.network.ApiClient;
import com.rocky.rxretrofit.support.retrofit.network.ObserverUtil;
import com.rocky.rxretrofit.support.retrofit.network.SingleCallback;
import com.rocky.rxretrofit.support.retrofit.network.WebserviceBuilder;
import com.rocky.rxretrofit.support.utils.AppLog;

import io.reactivex.disposables.CompositeDisposable;

public class SingleActivity extends AppCompatActivity implements SingleCallback {

    private CompositeDisposable compositeDisposable;
    private AppCompatTextView txtNameVal;
    private AppCompatTextView txtEmailVal;
    private AppCompatTextView txtPhoneHomeVal;
    private AppCompatTextView txtPhoneMobileVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        createReference();
        callAPI();
    }

    private void callAPI() {
        compositeDisposable = new CompositeDisposable();

        ObserverUtil
                .subscribeToSingle(ApiClient.getClient().create(WebserviceBuilder.class).getSingleObject()
                        , compositeDisposable, WebserviceBuilder.ApiNames.single, this);
    }

    private void createReference() {
        txtNameVal = (AppCompatTextView) findViewById(R.id.txt_name_val);
        txtEmailVal = (AppCompatTextView) findViewById(R.id.txt_email_val);
        txtPhoneHomeVal = (AppCompatTextView) findViewById(R.id.txt_phone_home_val);
        txtPhoneMobileVal = (AppCompatTextView) findViewById(R.id.txt_phone_mobile_val);
    }

    @Override
    public void onSingleSuccess(Object o, WebserviceBuilder.ApiNames apiNames) {
        switch (apiNames) {
            case single:
                ContactInfoModel contactInfoModel = (ContactInfoModel) o;
                if (contactInfoModel != null) {
                    txtNameVal.setText(contactInfoModel.getName());
                    txtEmailVal.setText(contactInfoModel.getEmail());
                    ContactInfoModel.PhoneBean phone = contactInfoModel.getPhone();
                    txtPhoneHomeVal.setText(phone.getHome());
                    txtPhoneMobileVal.setText(phone.getMobile());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onFailure(Throwable throwable, WebserviceBuilder.ApiNames apiNames) {
        AppLog.log(false, "SingleActivity " + "onFailure: ", throwable);
    }
}
