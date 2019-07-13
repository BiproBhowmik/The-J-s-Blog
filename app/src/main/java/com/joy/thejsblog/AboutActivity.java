package com.joy.thejsblog;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView facebookProfile, youtube, twiter, blog, facebookPage, instagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //back button
        getSupportActionBar().setTitle("About The Application Creator");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        facebookPage = findViewById(R.id.facebookPage);
        youtube = findViewById(R.id.youtube);
        twiter = findViewById(R.id.twiter);
        blog = findViewById(R.id.blog);
        instagram = findViewById(R.id.instagram);
        facebookProfile = findViewById(R.id.facebookProfile);

        facebookPage.setOnClickListener(this);
        youtube.setOnClickListener(this);
        twiter.setOnClickListener(this);
        blog.setOnClickListener(this);
        instagram.setOnClickListener(this);
        facebookProfile.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.facebookProfile) {
            goToUrl("https://www.facebook.com/biprobhowmikjoy");
        } else if (v.getId() == R.id.youtube) {
            goToUrl("https://www.youtube.com/channel/UC1rOLtMscFml6JcQA0hPWhg");
        } else if (v.getId() == R.id.twiter) {
            goToUrl("https://twitter.com/BiproBhowmik");
        } else if (v.getId() == R.id.blog) {
            goToUrl("https://biprobhj.blogspot.com/");
        }else if (v.getId() == R.id.instagram) {
            goToUrl("https://www.instagram.com/joy_bhowmik5/");
        }else if (v.getId() == R.id.facebookPage) {
            goToUrl("https://www.facebook.com/IamJoY.joybhowmik/?ref=bookmarks");
        }

        /*



         */

    }

    private void goToUrl(String s) {
        Uri url = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, url));
    }
}
