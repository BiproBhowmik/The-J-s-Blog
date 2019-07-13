package com.joy.thejsblog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView username,show_message;
    public CircleImageView circleImage;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);

        username = itemView.findViewById(R.id.messageUsername);
        show_message = itemView.findViewById(R.id.messageMessage);
        circleImage = itemView.findViewById(R.id.messageImage);
    }
}
