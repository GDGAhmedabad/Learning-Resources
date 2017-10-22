package com.jalotsav.fireauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jalotsav.fireauth.common.AppConstants;
import com.jalotsav.fireauth.common.GeneralFunctions;
import com.jalotsav.fireauth.common.LogManager;
import com.jalotsav.fireauth.model.MdlUsers;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.Arrays;

public class FireSignin extends AppCompatActivity implements AppConstants, View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    CoordinatorLayout mCrdntrlyot;
    TextInputLayout mTxtinptlyotEmail, mTxtinptlyotPaswrd;
    TextInputEditText mTxtinptEtEmail, mTxtinptEtPaswrd;
    AppCompatButton mAppcmptbtnSignup;
    TextView mTvFrgtPaswrd;
    ImageView mImgvwFirebase, mImgvwGp, mImgvwFb;
    ProgressBar mPrgrsbrMain;
    FirebaseAuth mFireAuth;
    FirebaseDatabase mFireDB;
    GoogleApiClient mGoogleApiClient;
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_fire_signin);

        mCrdntrlyot = (CoordinatorLayout) findViewById(R.id.cordntrlyot_fireauth);
        mTxtinptlyotEmail = (TextInputLayout) findViewById(R.id.txtinputlyot_fireauth_email);
        mTxtinptlyotPaswrd = (TextInputLayout) findViewById(R.id.txtinputlyot_fireauth_password);
        mTxtinptEtEmail = (TextInputEditText) findViewById(R.id.txtinptet_fireauth_email);
        mTxtinptEtPaswrd = (TextInputEditText) findViewById(R.id.txtinptet_fireauth_password);
        mAppcmptbtnSignup = (AppCompatButton) findViewById(R.id.appcmptbtn_fireauth_signin);
        mTvFrgtPaswrd = (TextView) findViewById(R.id.tv_fireauth_frgtpaswrd);
        mImgvwFirebase = (ImageView) findViewById(R.id.imgvw_fireauth_firebase);
        mImgvwGp = (ImageView) findViewById(R.id.imgvw_fireauth_social_gp);
        mImgvwFb = (ImageView) findViewById(R.id.imgvw_fireauth_social_fb);
        mPrgrsbrMain = (ProgressBar) findViewById(R.id.prgrsbr_fireauth);

        mFireAuth = FirebaseAuth.getInstance();
        mFireDB = FirebaseDatabase.getInstance();

        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAppcmptbtnSignup.setOnClickListener(this);
        mTvFrgtPaswrd.setOnClickListener(this);
        mImgvwFirebase.setOnClickListener(this);
        mImgvwGp.setOnClickListener(this);
        mImgvwFb.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.appcmptbtn_fireauth_signin:

                signInFirebase();
                break;
            case R.id.tv_fireauth_frgtpaswrd:

                startActivity(new Intent(this, FireResetPassword.class));
                break;
            case R.id.imgvw_fireauth_firebase:

                startActivity(new Intent(this, FireSignup.class));
                break;
            case R.id.imgvw_fireauth_social_gp:

                signInGoogle();
                break;
            case R.id.imgvw_fireauth_social_fb:

                signInFacebook();
                break;
        }
    }

    // Check all field Validation and Signin Firebase
    private void signInFirebase() {
        // TODO Auto-generated method stub

        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }

        String emailVal = mTxtinptEtEmail.getText().toString().trim();
        String passwordVal = mTxtinptEtPaswrd.getText().toString().trim();

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mFireAuth.signInWithEmailAndPassword(emailVal, passwordVal)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()) {

                            mPrgrsbrMain.setVisibility(View.GONE);
                            Log.w(LOG_TAG, "signInWithFirebase", task.getException());
                            Snackbar.make(mCrdntrlyot, "Authentication failed.\n" + task.getException().getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        } else
                            successLoginGetData(task);
                    }
                });
    }

    // Check validation for Email
    private boolean validateEmail() {

        if (mTxtinptEtEmail.getText().toString().trim().isEmpty()) {
            mTxtinptlyotEmail.setErrorEnabled(true);
            mTxtinptlyotEmail.setError(getString(R.string.email_entr_sml));
            requestFocus(mTxtinptEtEmail);
            return false;
        } else if (!GeneralFunctions.isvalidEmailFormat(mTxtinptEtEmail.getText().toString().trim())) {
            mTxtinptlyotEmail.setErrorEnabled(true);
            mTxtinptlyotEmail.setError(getString(R.string.email_invalid));
            requestFocus(mTxtinptEtEmail);
            return false;
        } else {
            mTxtinptlyotEmail.setError(null);
            mTxtinptlyotEmail.setErrorEnabled(false);
            return true;
        }
    }

    // Check validation for Password
    private boolean validatePassword() {

        if (mTxtinptEtPaswrd.getText().toString().trim().isEmpty()) {
            mTxtinptlyotPaswrd.setErrorEnabled(true);
            mTxtinptlyotPaswrd.setError(getString(R.string.password_entr_sml));
            requestFocus(mTxtinptEtPaswrd);
            return false;
        } else {
            mTxtinptlyotPaswrd.setError(null);
            mTxtinptlyotPaswrd.setErrorEnabled(false);
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void signInGoogle() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUESTCODE_SIGN_IN_GOOGLE);
    }

    private void signInFacebook() {

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // Facebook Sign In was successful, authenticate with Firebase
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                LogManager.printLog(LOGTYPE_DEBUG, "facebook:onCancel");
                mPrgrsbrMain.setVisibility(View.GONE);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(LOG_TAG, "facebook:onError", error);
                mPrgrsbrMain.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                mPrgrsbrMain.setVisibility(View.GONE);
                Snackbar.make(mCrdntrlyot, "Google authentication failed.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        mPrgrsbrMain.setVisibility(View.VISIBLE);

        LogManager.printLog(LOGTYPE_INFO, "signInWithGoogleId: " + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFireAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        LogManager.printLog(LOGTYPE_DEBUG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {

                            mPrgrsbrMain.setVisibility(View.GONE);
                            Log.w(LOG_TAG, "signInWithCredential", task.getException());
                            Snackbar.make(mCrdntrlyot, "Authentication failed.\n" + task.getException().getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        } else
                            successLoginGetData(task);
                    }
                });
    }

    private void firebaseAuthWithFacebook(AccessToken accessToken) {

        mPrgrsbrMain.setVisibility(View.VISIBLE);

        LogManager.printLog(LOGTYPE_INFO, "signInWithFacebookToken: " + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFireAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        LogManager.printLog(LOGTYPE_DEBUG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {

                            mPrgrsbrMain.setVisibility(View.GONE);
                            Log.w(LOG_TAG, "signInWithCredential", task.getException());
                            Snackbar.make(mCrdntrlyot, "Authentication failed.\n" + task.getException().getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        } else
                            successLoginGetData(task);
                    }
                });
    }

    private void successLoginGetData(Task<AuthResult> task) {

        FirebaseUser fireUser = task.getResult().getUser();
        DatabaseReference mDBUsersRef = mFireDB.getReference("ColUsers");

        String photoUrl = "";
        try {
            photoUrl = fireUser.getPhotoUrl().getEncodedSchemeSpecificPart();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        mDBUsersRef.child(fireUser.getUid()).setValue(
                new MdlUsers(fireUser.getDisplayName(), fireUser.getEmail(), photoUrl));

        Toast.makeText(this, "Successfully Sign in.", Toast.LENGTH_SHORT).show();

        mPrgrsbrMain.setVisibility(View.GONE);

        mTxtinptEtEmail.setText("");
        mTxtinptEtPaswrd.setText("");

        startActivity(new Intent(this, FireHome.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mFireAuth == null)
            mPrgrsbrMain.setVisibility(View.GONE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        LogManager.printLog(LOGTYPE_DEBUG, "onConnectionFailed:" + connectionResult);
    }

    // Get Key Hashes for FacebookIntegration
    /*private void getKeyHashes() {

        PackageInfo info;
        try {

            info = getPackageManager().getPackageInfo(
                    this.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                LogManager.printLog(LOGTYPE_INFO, "Hash key: " + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            LogManager.printLog(LOGTYPE_INFO, "name not found: " + e1.toString());
        } catch (NoSuchAlgorithmException e) {
            LogManager.printLog(LOGTYPE_INFO, "no such an algorithm: " + e.toString());
        } catch (Exception e) {
            LogManager.printLog(LOGTYPE_INFO, "exception: " + e.toString());
        }
    }*/
}
