package com.joy.thejsblog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersViewHolder> {

    private Context context;
    private List<StoreToDB_UsersInfo> allUserList;
    private FirebaseDatabase database;
    private int lastPosition = -1;

    public AllUsersAdapter(Context context, List<StoreToDB_UsersInfo> allUserList) {
        this.context = context;
        Collections.sort(allUserList);
        this.allUserList = allUserList;
    }

    @NonNull
    @Override
    public AllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.all_users_raw, viewGroup, false);


        return new AllUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersViewHolder allUsersViewHolder, final int i) {

        final StoreToDB_UsersInfo storeToDB_usersInfo = allUserList.get(i);
        final String susername = storeToDB_usersInfo.getUserName();
        final String semail = storeToDB_usersInfo.getUserEmail();
        final String simgurl = storeToDB_usersInfo.getImageUrl();

        allUsersViewHolder.allUserCardUsername.setText(susername);
        allUsersViewHolder.allUserCardEmail.setText(semail);
        Picasso.with(context)
                .load(simgurl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(allUsersViewHolder.allUserCardImage);

        //setAnimation(allUsersViewHolder.itemView, i);

        allUsersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("uuuuu"+semail+susername+simgurl);
                Intent intent = new Intent(context, UserDemoProfileActivity.class);
                intent.putExtra("UID", storeToDB_usersInfo.getUserID());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //why??
                context.startActivity(intent);
            }
        });


    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return allUserList.size();
    }

    public void filterList(List<StoreToDB_UsersInfo> filteredList)
    {
        allUserList = filteredList;
        notifyDataSetChanged();
    }
}
