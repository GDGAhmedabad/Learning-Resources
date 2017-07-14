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
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.squareup.picasso.Picasso;

/**
 * Created by jalotsav on 17/10/16.
 */

public class FireHome extends AppCompatActivity {

    ImageView mImgvwProfile;
    TextView mTvDisplayName, mTvEmail;
    FirebaseAuth mFireAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_fire_auth_home);

        mImgvwProfile = (ImageView) findViewById(R.id.imgvw_fireauth_home_profile);
        mTvDisplayName = (TextView) findViewById(R.id.tv_fireauth_home_displayname);
        mTvEmail = (TextView) findViewById(R.id.tv_fireauth_home_email);

        mFireAuth = FirebaseAuth.getInstance();
        FirebaseUser fireUser = mFireAuth.getCurrentUser();
        if (fireUser != null) {

            Picasso.with(this)
                    .load(fireUser.getPhotoUrl())
                    .placeholder(R.drawable.img_firebase_logo)
                    .into(mImgvwProfile);
            if(!TextUtils.isEmpty(fireUser.getDisplayName()))
                mTvDisplayName.setText(fireUser.getDisplayName());
            else mTvDisplayName.setVisibility(View.GONE);
            mTvEmail.setText(fireUser.getEmail());
        }
    }
}
