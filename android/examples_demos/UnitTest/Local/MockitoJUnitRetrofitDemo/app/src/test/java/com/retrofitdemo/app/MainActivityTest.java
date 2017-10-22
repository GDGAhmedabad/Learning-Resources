package com.retrofitdemo.app;

import com.retrofitdemo.api.response.StackOverflowUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Rathod on 04-Feb-17.
 */

@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {

    private MainActivityPresenter presenter;

    @Mock
    MainActivityView view;

    @Before
    public void setUp() throws Exception {
        presenter = new MainActivityPresenter(view);

    }

    @Test
    public void testOrder() throws Exception {
        when(view.getOrder()).thenReturn("");

        presenter.getSOUserData();

        verify(view).showEmptyOrderError(R.string.error_empty_order);
    }

    @Test
    public void testSortBy() throws Exception {
        when(view.getOrder()).thenReturn("desc");
        when(view.getSortBy()).thenReturn("");

        presenter.getSOUserData();

        verify(view).showEmptySortByError(R.string.error_empty_sortby);
    }

    @Test
    public void testSite() throws Exception {
        when(view.getOrder()).thenReturn("desc");
        when(view.getSortBy()).thenReturn("reputation");
        when(view.getSite()).thenReturn("");

        presenter.getSOUserData();

        verify(view).showEmptySiteError(R.string.error_empty_site);

    }

    @Test
    public void testGetSoUserData() throws Exception {

        when(view.getOrder()).thenReturn("desc");
        when(view.getSortBy()).thenReturn("reputation");
        when(view.getSite()).thenReturn("stackoverflow");

        presenter.getSOUserData();

        verify(view).getSOUserData("desc", "reputation", "stackoverflow");

        Response<StackOverflowUser> response =
                Response.success(Mockito.mock(StackOverflowUser.class));

        presenter.proceedUserResponse(response);

//        when(response.body().getItems().size()).thenReturn(0);

//        verify(view).setUserData(response.body().getItems());
        verify(view).showEmptyUserList(R.string.error_empty_user_list);
    }

    @Test
    public void testUserDataFailure() throws Exception {


        when(view.getOrder()).thenReturn("desc");
        when(view.getSortBy()).thenReturn("reputation");
        when(view.getSite()).thenReturn("stackoverflow");

        presenter.getSOUserData();

        verify(view).getSOUserData("desc", "reputation", "stackoverflow");

        Response<StackOverflowUser> response =
                Response.error(404, Mockito.mock(ResponseBody.class));

        presenter.proceedUserResponse(response);

        verify(view).showUserDataError(R.string.error_no_response);
    }
}
