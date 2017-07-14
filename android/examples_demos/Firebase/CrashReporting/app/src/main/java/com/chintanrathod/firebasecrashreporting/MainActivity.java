package com.chintanrathod.firebasecrashreporting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

public class MainActivity extends AppCompatActivity {

    TextView dummyTextView;

    Button fatalError1, fatalError2, nonFatalError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fatalError1 = (Button) findViewById(R.id.fatal_error_1);
        fatalError2 = (Button) findViewById(R.id.fatal_error_2);
        nonFatalError = (Button) findViewById(R.id.non_fatal_error);

        fatalError1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dummyTextView.setText("This will fire NullPointerException");
            }
        });

        fatalError2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new ArrayIndexOutOfBoundsException();
            }
        });

        nonFatalError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dummyTextView == null){
                    FirebaseCrash.report(new Exception("dummyTextView is not initialized"));

                    Toast.makeText(MainActivity.this, "Custom exception is thrown", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
