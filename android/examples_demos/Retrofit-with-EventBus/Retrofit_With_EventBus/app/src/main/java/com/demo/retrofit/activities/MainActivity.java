package com.demo.retrofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.demo.retrofit.R;
import com.demo.retrofit.adapters.ViewPagerAdapter;
import com.demo.retrofit.fragments.ImagesFragment;
import com.demo.retrofit.fragments.UploadFragment;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    MenuItem prevMenuItem;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_list:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_upload:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_about:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());

        ImagesFragment imagesFragment = ImagesFragment.newInstance(2);
        UploadFragment uploadFragment = UploadFragment.newInstance();
        LibsFragment fragment = new LibsBuilder()
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withAboutDescription(
                        "The sample mainly focused on how <b>Retrofit with EventBus</b> can work. " +
                                "This project contains all the API handles. Also some extra utils included. " +
                                "The sample project should be considered in action when you have to architect your own project with the All APIs.<br /><br />" +
                                "One may find this structure bit complex but once got used to it, it'll be the matter of minutes to implement the APIs.<br /><br />" +
                                "<a href='https://github.com/DearDhruv'>https://github.com/DearDhruv</a><br /><br />" +
                                "Application and API created by <b><a href='https://www.linkedin.com/in/deardhruv/'>DearDhruv</a></b>")
                .fragment();

        adapter.addFragment(imagesFragment);
        adapter.addFragment(uploadFragment);
        adapter.addFragment(fragment);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (prevMenuItem != null) {
                prevMenuItem.setChecked(false);
            } else {
                navigation.getMenu().getItem(0).setChecked(false);
            }

            navigation.getMenu().getItem(position).setChecked(true);
            prevMenuItem = navigation.getMenu().getItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
