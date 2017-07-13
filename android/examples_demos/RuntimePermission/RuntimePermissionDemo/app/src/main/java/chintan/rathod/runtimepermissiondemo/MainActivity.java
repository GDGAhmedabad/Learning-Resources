package chintan.rathod.runtimepermissiondemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chintan Rathod
 */
public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int READ_CONTACT_REQUEST_CODE = 100;
    private Button btnContacts;
    private TextView txtContactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnContacts = (Button) findViewById(R.id.btnContacts);
        txtContactList = (TextView) findViewById(R.id.txtContactList);

        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Check build version
                 */
                if(Build.VERSION.SDK_INT < 23){
                    displayContacts();
                }else {
                    requestContactPermission();
                }
            }
        });
    }

    private void requestContactPermission() {

        int hasContactPermission = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS);

        if(hasContactPermission == PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(MainActivity.this, "Contact Permission is already granted", Toast.LENGTH_LONG).show();
            displayContacts();
        }else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("Contact permission is require to show demo in GDGAhmedabad DevFest 2015.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_REQUEST_CODE);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing, user does not want to request
                            }
                        }).create();
                dialog.show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_REQUEST_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){
            case READ_CONTACT_REQUEST_CODE:

                // Check if the only required permission has been granted
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has been granted, preview can be displayed
                    Log.i("Permission", "Contact permission has now been granted. Showing result.");
                    Toast.makeText(MainActivity.this,"Contact Permission is Granted",Toast.LENGTH_SHORT).show();

                    displayContacts();

                } else {
                    Log.i("Permission", "Contact permission was NOT granted.");
                    Toast.makeText(MainActivity.this,"Permission is not Granted",Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private void displayContacts() {
        txtContactList.setText("");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        txtContactList.append(name + " : " + phoneNo + "\n");
                    }
                    pCur.close();
                }
            }
        }
    }
}
