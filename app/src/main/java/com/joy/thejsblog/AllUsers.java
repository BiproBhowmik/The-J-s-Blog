package com.joy.thejsblog;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllUsers extends AppCompatActivity {

    private List<StoreToDB_UsersInfo> userList;
    private DatabaseReference allUsers;
    private FirebaseUser firebaseUser;

    private RecyclerView userRecyclerView;
    private EditText searchViewEditText;

    private String userKey;

    private AllUsersAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        searchViewEditText = findViewById(R.id.searchEditText);

        userRecyclerView = findViewById(R.id.usersRecyclerViewID);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //back button
        getSupportActionBar().setTitle("All Users Identity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userList = new ArrayList();
        allUsers = FirebaseDatabase.getInstance().getReference().child("Users");

        final SharedPreferences sharedPreferences = getSharedPreferences("myFileUNandEM", Context.MODE_PRIVATE);

        allUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    StoreToDB_UsersInfo upload = dataSnapshot1.getValue(StoreToDB_UsersInfo.class);
                    userList.add(upload);

                    if(upload.getUserEmail().equals(sharedPreferences.getString("email", "Unknown")))
                    {
                        userKey = upload.getKey();
                    }
                }

                myAdapter = new AllUsersAdapter(getApplicationContext(), userList);

                userRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        ///For Search EditText
        searchViewEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterx(s.toString());
            }
        });


    }

    //by me
    private void activeOrNot() {

        DatabaseReference connecteRef = FirebaseDatabase.getInstance().getReference(".info/connected");

        connecteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userKey);
                    //StoreToDB_UsersInfo upload = ref;

                } else {



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void filterx(String text) {

        List<StoreToDB_UsersInfo> filteredList = new ArrayList<>();

        for(StoreToDB_UsersInfo item : userList)
        {
            if(item.getUserName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }

        myAdapter.filterList(filteredList);
    }
}
