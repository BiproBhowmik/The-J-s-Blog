package com.joy.thejsblog;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {

    private String sUID;

    private DatabaseReference reference;

    private CircleImageView profilePicture;
    private TextView userName, eMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        profilePicture = findViewById(R.id.profileImage);
        userName = findViewById(R.id.profileUsername);
        eMail = findViewById(R.id.profileEmail);

        Bundle bundle = getIntent().getExtras();

        sUID = bundle.getString("UID");

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    StoreToDB_UsersInfo storeToDB_usersInfo = dataSnapshot1.getValue(StoreToDB_UsersInfo.class);
                    if(sUID.equals(storeToDB_usersInfo.getUserID()))
                    {
                        userName.setText(storeToDB_usersInfo.getUserName());
                        eMail.setText(storeToDB_usersInfo.getUserEmail());

                        Picasso.with(MyProfileActivity.this)
                                .load(storeToDB_usersInfo.getImageUrl())
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(profilePicture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyProfileActivity.this, "Somethinf Went Wrong", Toast.LENGTH_LONG).show();
            }
        });

    }
}
