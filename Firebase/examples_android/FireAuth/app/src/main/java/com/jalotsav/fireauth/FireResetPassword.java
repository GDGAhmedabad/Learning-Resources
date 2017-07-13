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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.jalotsav.fireauth.common.GeneralFunctions;

/**
 * Created by jalotsav on 17/10/16.
 */

public class FireResetPassword extends AppCompatActivity {

    CoordinatorLayout mCrdntrlyot;
    TextInputLayout mTxtinptlyotEmail;
    TextInputEditText mTxtinptEtEmail;
    AppCompatButton mAppcmptbtnSignup;
    ProgressBar mPrgrsbrMain;
    FirebaseAuth mFireAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_fire_auth_resetpassword);

        mCrdntrlyot = (CoordinatorLayout) findViewById(R.id.cordntrlyot_fireauth_resetpaswrd);
        mTxtinptlyotEmail = (TextInputLayout) findViewById(R.id.txtinputlyot_fireauth_resetpaswrd_email);
        mTxtinptEtEmail = (TextInputEditText) findViewById(R.id.txtinptet_fireauth_resetpaswrd_email);
        mAppcmptbtnSignup = (AppCompatButton) findViewById(R.id.appcmptbtn_fireauth_resetpaswrd);
        mPrgrsbrMain = (ProgressBar) findViewById(R.id.prgrsbr_fireauth_resetpaswrd);

        mFireAuth = FirebaseAuth.getInstance();

        mAppcmptbtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check all field Validation and call API
                checkVldtnCallApi();
            }
        });

    }

    // Check all field Validation and call API
    private void checkVldtnCallApi() {
        // TODO Auto-generated method stub

        if (!validateEmail()) {
            return;
        }

        String emailVal = mTxtinptEtEmail.getText().toString().trim();

        mPrgrsbrMain.setVisibility(View.VISIBLE);
        mFireAuth.sendPasswordResetEmail(emailVal)
                .addOnCompleteListener(FireResetPassword.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(!task.isSuccessful())
                            Snackbar.make(mCrdntrlyot, "Failed to send reset email!", Snackbar.LENGTH_LONG).show();
                        else {

                            Toast.makeText(FireResetPassword.this, "We have sent you instructions to reset your password!",
                                    Toast.LENGTH_SHORT).show();
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
