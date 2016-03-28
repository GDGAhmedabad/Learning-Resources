package com.admobdemo.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView ivMyList;
    private ArrayList<Object> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivMyList = (ListView) findViewById(R.id.ivMyList);

        setUserListWithAdMob();
    }

    private void setUserListWithAdMob() {

        userList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                userList.add(null);
            } else {
                ModelUser user = new ModelUser();
                user.setUserEmail("Myemail" + i + "@gmail.com");
                user.setUserName("Pranay");
                user.setUserPhone("999988889" + i);
                userList.add(user);
            }
        }

        AdapterListView adapter = new AdapterListView(MainActivity.this, userList);
        ivMyList.setAdapter(adapter);

    }
}
