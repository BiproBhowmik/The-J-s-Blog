package com.joy.thejsblog;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PostLarger extends AppCompatActivity {

    private String stitle, sdiscription, simageURI, sUsername, sEmail;
    private TextView username, email, title, discription;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_larger);

        //back button
        getSupportActionBar().setTitle("Expended Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        stitle = bundle.getString("title");
        sdiscription = bundle.getString("discription");
        simageURI = bundle.getString("image");
        sUsername = bundle.getString("username");
        sEmail = bundle.getString("email");

        username = findViewById(R.id.lpUsername);
        email = findViewById(R.id.lpdEmail);
        title = findViewById(R.id.lpTitle);
        discription = findViewById(R.id.lpDiscription);
        imageView = findViewById(R.id.lpImageView);

        username.setText(sUsername);
        email.setText(sEmail);
        title.setText(stitle);
        discription.setText(sdiscription);

        Picasso.with(getApplicationContext())
                .load(simageURI)
                .placeholder(R.mipmap.ic_launcher)
//                .fit()
//                .centerCrop()
                .into(imageView);

    }
}
