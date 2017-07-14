/*
 * Copyright 2016 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jalotsav.fireauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jalotsav.fireauth.common.GeneralFunctions;
import com.jalotsav.fireauth.model.MdlUsers;

/**
 * Created by jalotsav on 17/10/16.
 */

public class FireSignup extends AppCompatActivity implements View.OnClickListener {

    CoordinatorLayout mCrdntrlyot;
    TextInputLayout mTxtinptlyotEmail, mTxtinptlyotPaswrd;
    TextInputEditText mTxtinptEtEmail, mTxtinptEtPaswrd;
    AppCompatButton mAppcmptbtnSignup;
    TextView mTvFrgtPaswrd, mTvSigninMe;
    ProgressBar mPrgrsbrMain;
    FirebaseAuth mFireAuth;
    FirebaseDatabase mFireDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_fire_auth_signup);

        mCrdntrlyot = (CoordinatorLayout) findViewById(R.id.cordntrlyot_fireauth_signup);
        mTxtinptlyotEmail = (TextInputLayout) findViewById(R.id.txtinputlyot_fireauth_signup_email);
        mTxtinptlyotPaswrd = (TextInputLayout) findViewById(R.id.txtinputlyot_fireauth_signup_password);
        mTxtinptEtEmail = (TextInputEditText) findViewById(R.id.txtinptet_fireauth_signup_email);
        mTxtinptEtPaswrd = (TextInputEditText) findViewById(R.id.txtinptet_fireauth_signup_password);
        mAppcmptbtnSignup = (AppCompatButton) findViewById(R.id.appcmptbtn_fireauth_signup);
        mTvFrgtPaswrd = (TextView) findViewById(R.id.tv_fireauth_signup_frgtpaswrd);
        mTvSigninMe = (TextView) findViewById(R.id.tv_fireauth_signup_signinme);
        mPrgrsbrMain = (ProgressBar) findViewById(R.id.prgrsbr_fireauth_signup);

        mFireAuth = FirebaseAuth.getInstance();
        mFireDB = FirebaseDatabase.getInstance();

        mAppcmptbtnSignup.setOnClickListener(this);
        mTvFrgtPaswrd.setOnClickListener(this);
        mTvSigninMe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.appcmptbtn_fireauth_signup:

                // Check all field Validation and call API
                checkVldtnCallApi();
                break;
            case R.id.tv_fireauth_signup_frgtpaswrd:

                startActivity(new Intent(FireSignup.this, FireResetPassword.class));
                break;
            case R.id.tv_fireauth_signup_signinme:

                onBackPressed();
                break;
        }
    }

    // Check all field Validation and call API
    private void checkVldtnCallApi() {
        // TODO Auto-generated method stub

        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }

        final String emailVal = mTxtinptEtEmail.getText().toString().trim();
        String passwordVal = mTxtinptEtPaswrd.getText().toString().trim();

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mFireAuth.createUserWithEmailAndPassword(emailVal, passwordVal)
                .addOnCompleteListener(FireSignup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(!task.isSuccessful()) {
                            Snackbar.make(mCrdntrlyot, "Authentication failed.\n" + task.getException().getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        } else {

                            FirebaseUser fireUser = task.getResult().getUser();
                            DatabaseReference mDBUsersRef = mFireDB.getReference("ColUsers");
                            mDBUsersRef.child(fireUser.getUid()).setValue(new MdlUsers("", emailVal, ""));

                            Toast.makeText(FireSignup.this, "Successfully Registered.", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        mPrgrsbrMain.setVisibility(View.GONE);
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

    @Override
    protected void onResume() {
        super.onResume();

        mPrgrsbrMain.setVisibility(View.GONE);
    }
}
