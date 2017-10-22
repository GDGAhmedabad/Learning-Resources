package com.rocky.rxretrofit.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rocky.rxretrofit.R;
import com.rocky.rxretrofit.support.retrofit.model.ContactInfoModel;
import com.rocky.rxretrofit.support.retrofit.network.ApiClient;
import com.rocky.rxretrofit.support.retrofit.network.ListCallback;
import com.rocky.rxretrofit.support.retrofit.network.ObserverUtil;
import com.rocky.rxretrofit.support.retrofit.network.WebserviceBuilder;
import com.rocky.rxretrofit.support.utils.AppLog;
import com.rocky.rxretrofit.ui.adapter.ContactInfoAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class ListActivity extends AppCompatActivity implements ListCallback {

    private CompositeDisposable compositeDisposable;
    private ListActivity activity;
    private ContactInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        activity = this;
        createReference();
        callAPI();
    }

    private void createReference() {
        RecyclerView listContact = (RecyclerView) findViewById(R.id.list_contact);
        listContact.setHasFixedSize(false);
        listContact.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ContactInfoAdapter(activity, new ArrayList<ContactInfoModel>());
        listContact.setAdapter(adapter);
        listContact.addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity)
                .colorProvider(adapter)
                .build());
    }

    private void callAPI() {
        compositeDisposable = new CompositeDisposable();

        Observable<List<ContactInfoModel>> listObject = ApiClient.getClient().create(WebserviceBuilder.class).getListObject();
        ObserverUtil
                .subscribeToList(listObject
                        , compositeDisposable, WebserviceBuilder.ApiNames.list, this);
    }

    @Override
    public void onListNext(Object object, WebserviceBuilder.ApiNames apiNames) {
        switch (apiNames) {
            case list:
                ContactInfoModel contactInfoModel = (ContactInfoModel) object;
                adapter.addItem(contactInfoModel);
                break;
        }
    }

    @Override
    public void onFailure(Throwable throwable, WebserviceBuilder.ApiNames apiNames) {
        AppLog.log(false, "ListActivity " + "onFailure: ", throwable);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onListComplete(WebserviceBuilder.ApiNames apiNames) {

    }
}
