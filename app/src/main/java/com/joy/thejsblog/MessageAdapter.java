package com.joy.thejsblog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private Context context;
    private List<MessageStore> chats;

    public MessageAdapter(Context context, List<MessageStore> chats) {
        this.context = context;
        this.chats = chats;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.meaasge_raw, viewGroup, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        MessageStore messageStore = chats.get(i);

        messageViewHolder.username.setText(messageStore.getuName());
        messageViewHolder.show_message.setText(messageStore.getmSG());

        Picasso.with(context)
                .load(messageStore.getiURL())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(messageViewHolder.circleImage);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
