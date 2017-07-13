package chintan.rathod.appindexingdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeakerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaker_activity);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        TextView txtOutput = (TextView) findViewById(R.id.txtOutput);
        ImageView imgSpeaker = (ImageView) findViewById(R.id.imgSpeaker);

        String speakerName = data.toString().substring(data.toString().lastIndexOf("=") + 1);

        txtOutput.setText(speakerName);

        if(speakerName.equalsIgnoreCase("pareshmayani")){
            imgSpeaker.setImageResource(R.drawable.paresh_mayani);
        }else if(speakerName.equalsIgnoreCase("chintanrathod")){
            imgSpeaker.setImageResource(R.drawable.chintan_rathod);
        }else if(speakerName.equalsIgnoreCase("utpalbetai")){
            imgSpeaker.setImageResource(R.drawable.utpal_betai);
        }else if(speakerName.equalsIgnoreCase("dhrumilshah")){
            imgSpeaker.setImageResource(R.drawable.dhrumil_shah);
        }
    }
}
