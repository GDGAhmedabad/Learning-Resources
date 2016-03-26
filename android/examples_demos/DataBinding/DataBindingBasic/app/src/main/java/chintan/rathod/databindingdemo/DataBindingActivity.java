package chintan.rathod.databindingdemo;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import chintan.rathod.databindingdemo.Model.User;
import chintan.rathod.databindingdemo.databinding.MainActivityBinding;

public class DataBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        MainActivityBinding binding =
                DataBindingUtil.setContentView(this, R.layout.main_activity);
        User user = new User("Chintan Rathod", 27);
        binding.setUser(user);
    }
}
