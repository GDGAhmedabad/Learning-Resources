package com.rrr.databindingonclick;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rrr.databindingonclick.Model.User;
import com.rrr.databindingonclick.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);
        user= new User("Ravi","Rupareliya");
        binding.setUser(user);
    }
    public void updateValue(View v)
    {
        user.setFirstName("GDG");
        user.setLastName("Ahmedabad");
    }
}
