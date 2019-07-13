package com.joy.thejsblog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogViewHolder> {

    private int loveCou;
    private int lastPosition = -1;

    private Context context;
    private List<StoreToDB> databaseList;

    private DatabaseReference database;

    public BlogAdapter(Context context, List<StoreToDB> databaseList) {
        this.context = context;
        Collections.reverse(databaseList);
        this.databaseList = databaseList;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.blog_row, viewGroup, false);


        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BlogViewHolder blogViewHolder, final int i) {

        final StoreToDB storeToDB = databaseList.get(i);
        final String stitle = storeToDB.getTitle();
        final String sdiscription = storeToDB.getDiscription();
        final String simageURI = storeToDB.getImageURI();
        final String sUsername = storeToDB.getUserName();
        final String sEmail = storeToDB.getEmail();
        final String sKey = storeToDB.getKey();
        final String sDate = storeToDB.getDate();
        final String sTime = storeToDB.getTime();
        final String sCircleImage = storeToDB.getCirleImage();


        blogViewHolder.title.setText(stitle);
        blogViewHolder.description.setText(sdiscription);
        blogViewHolder.username.setText(sUsername);
        blogViewHolder.email.setText(sEmail);
        blogViewHolder.date.setText(sDate);
        blogViewHolder.time.setText(sTime);

        Picasso.with(context)
                .load(simageURI)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(blogViewHolder.imageView);

        Picasso.with(context)
                .load(sCircleImage)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(blogViewHolder.circleImageView);

        setAnimation(blogViewHolder.itemView, i);

        blogViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostLarger.class);
                intent.putExtra("username", sUsername);
                intent.putExtra("email", sEmail);
                intent.putExtra("title", stitle);
                intent.putExtra("discription", sdiscription);
                intent.putExtra("image", simageURI);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //why??
                context.startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance().getReference().child("Love Reacts").child(sKey);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                blogViewHolder.loveCounter.setText(dataSnapshot.getChildrenCount() + " Peolpe's Loved It");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        blogViewHolder.loveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blogViewHolder.loveButton.startAnimation(AnimationUtils.loadAnimation(context, R.anim.simple_animation));
                String eMail, uName;
                SharedPreferences sharedPreferences = context.getSharedPreferences("myFileUNandEM", Context.MODE_PRIVATE);
                eMail = sharedPreferences.getString("email", "Unknown");
                uName = sharedPreferences.getString("username", "Unknown");

                database = FirebaseDatabase.getInstance().getReference().child("Love Reacts").child(sKey);

                database.child(sKey+uName).setValue(new Love("yes", sKey, 0));

                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        blogViewHolder.loveCounter.setText(dataSnapshot.getChildrenCount() + " Peolpe's Loved It");
                        System.out.println("Count = "+ dataSnapshot.getChildrenCount() + " && ");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return databaseList.size();
    }

    public void filterListn(List<StoreToDB> filteredList)
    {
        databaseList = filteredList;
        notifyDataSetChanged();
    }

}
