package com.retrofitdemo.app;

import com.retrofitdemo.api.response.StackOverflowUser;

import java.util.List;

/**
 * @author Rathod on 04-Feb-17.
 */

public interface MainActivityView {

    String getOrder();

    void showEmptyOrderError(int error_empty_order);

    String getSortBy();

    void showEmptySortByError(int error_empty_sortby);

    String getSite();

    void showEmptySiteError(int error_empty_site);

    void getSOUserData(String order, String sortBy, String site);

    void setUserData(List<StackOverflowUser.Item> items);

    void showUserDataError(int error_no_response);

    void showEmptyUserList(int error_empty_user_list);

}
