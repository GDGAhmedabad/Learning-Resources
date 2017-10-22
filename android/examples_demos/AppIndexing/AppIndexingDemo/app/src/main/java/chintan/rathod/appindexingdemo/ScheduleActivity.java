package chintan.rathod.appindexingdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        TextView txtOutput = (TextView) findViewById(R.id.txtOutput);
        txtOutput.setText("Action : " + action + "\n Data : " + ((data == null)?"":data.toString()));

    }
}
