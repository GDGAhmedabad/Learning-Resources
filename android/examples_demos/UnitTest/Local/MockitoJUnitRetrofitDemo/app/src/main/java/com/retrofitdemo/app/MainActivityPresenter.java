package com.retrofitdemo.app;

import com.retrofitdemo.api.response.StackOverflowUser;

import retrofit2.Response;

/**
 * @author Rathod on 04-Feb-17.
 */

public class MainActivityPresenter {

    private MainActivityView view;

    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }

    public void getSOUserData(){

        String order = view.getOrder();

        if(order.isEmpty()){
            view.showEmptyOrderError(R.string.error_empty_order);
            return;
        }

        String sortBy = view.getSortBy();

        if(sortBy.isEmpty()){
            view.showEmptySortByError(R.string.error_empty_sortby);
            return;
        }

        String site = view.getSite();

        if(site.isEmpty()){
            view.showEmptySiteError(R.string.error_empty_site);
            return;
        }

        view.getSOUserData(order, sortBy, site);
    }

    public void proceedUserResponse(Response<StackOverflowUser> response) {
        if(response != null) {
            if (response.isSuccess()) {
                if(response.body().getItems().size() > 0) {
                    view.setUserData(response.body().getItems());
                }else{
                    view.showEmptyUserList(R.string.error_empty_user_list);
                }
            } else {
                view.showUserDataError(R.string.error_no_response);
            }
        }else{
            view.showUserDataError(R.string.error_no_response);
        }
    }
}
