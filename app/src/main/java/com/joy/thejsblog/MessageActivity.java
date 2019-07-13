package com.joy.thejsblog;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText message;
    private ImageView sent;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference, reference;

    private List<MessageStore> mChats;
    private RecyclerView messageRecyView;

    private String sentUserID, currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mChats = new ArrayList<>();
        messageRecyView = findViewById(R.id.messageRecyView);
        messageRecyView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageRecyView.setLayoutManager(linearLayoutManager);

        //back button
        getSupportActionBar().setTitle("Group Messenger");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Message Info");

        message = findViewById(R.id.editTextMessage);
        sent = findViewById(R.id.imageButtonSentMessage);

        sent.setOnClickListener(this);

        showMessage();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.imageButtonSentMessage)
        {
            String messageS = message.getText().toString();

            if(!messageS.isEmpty())
            {

                SharedPreferences sharedPreferences = getSharedPreferences("myFileUNandEM", Context.MODE_PRIVATE);
                String userName = sharedPreferences.getString("username", "Unknown");
                String imageUrl = sharedPreferences.getString("image", "Unknown");

                MessageStore messageStore = new MessageStore(userName, messageS, imageUrl);

                String key = databaseReference.push().getKey();

                databaseReference.child(key).setValue(messageStore);
                Toast.makeText(getApplicationContext(), "Message Stored", Toast.LENGTH_SHORT).show();
                message.setText(null);
            }
        }
    }

    private void showMessage()
    {
        reference = FirebaseDatabase.getInstance().getReference().child("Message Info");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mChats.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    MessageStore upload = dataSnapshot1.getValue(MessageStore.class);
                    mChats.add(upload);
                }

                MessageAdapter myAdapter = new MessageAdapter(MessageActivity.this, mChats);
                messageRecyView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
