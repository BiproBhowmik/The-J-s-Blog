package com.joy.thejsblog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AllUsersViewHolder extends RecyclerView.ViewHolder {

    TextView allUserCardUsername, allUserCardEmail;
    ImageView allUserCardImage;


    public AllUsersViewHolder(@NonNull View itemView) {
        super(itemView);

        allUserCardUsername = itemView.findViewById(R.id.allUserCardUsername);
        allUserCardEmail = itemView.findViewById(R.id.allUserCardEmail);
        allUserCardImage = itemView.findViewById(R.id.allUserCardImage);
    }
}
