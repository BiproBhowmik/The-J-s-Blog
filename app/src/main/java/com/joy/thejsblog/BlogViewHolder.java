package com.joy.thejsblog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogViewHolder extends RecyclerView.ViewHolder {


    ImageView imageView, loveButton;
    TextView title, description, username, email, loveCounter, date, time;
    CircleImageView circleImageView;
    private DatabaseReference database;

    public BlogViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.cardImageView);
        title = itemView.findViewById(R.id.cardTitle);
        description = itemView.findViewById(R.id.cardDiscription);
        username = itemView.findViewById(R.id.cardUsername);
        email = itemView.findViewById(R.id.cardEmail);
        loveButton = itemView.findViewById(R.id.cardLove);
        loveCounter = itemView.findViewById(R.id.cardLoveCounter);
        date = itemView.findViewById(R.id.cardDate);
        time = itemView.findViewById(R.id.cardTime);
        circleImageView = itemView.findViewById(R.id.circle_image);
    }
}
