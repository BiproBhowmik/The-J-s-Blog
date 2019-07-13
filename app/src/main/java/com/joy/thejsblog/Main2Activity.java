package com.joy.thejsblog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stone.vega.library.VegaLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    private BlogAdapter myAdapter;

    private Menu menu;
    private boolean viewChange = false;

    private ProgressBar mainProgressbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private AlertDialog.Builder aleartDialogBuilder;
    private TextView nav_header_name, nav_header_email;
    private EditText searchEdittext;
    private ImageView nav_heder_image;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase, reference, profilePics;
    FirebaseDatabase db;
    private List<StoreToDB> databaseList;
    //private List<ForProfilePic> profilePicsList;
    NavigationView navigationView;
    //private ProgressDialog mProgress;
    //private String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        chkStatus(); //check internet
        //activeOrNot();

        //Edittext
        searchEdittext = findViewById(R.id.searchEditTextMain);
        searchEdittext.setVisibility(View.INVISIBLE);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);//refresh
        recyclerView = findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.item_layout_animation));
        //recyclerViewFriend.smoothScrollToPosition(0);
        recyclerView.smoothScrollToPosition(0);
        recyclerView.setLayoutManager(new VegaLayoutManager());

        mainProgressbar = findViewById(R.id.mainProgressbar);
        Sprite doubleBounce = new DoubleBounce();
        Sprite cubeGrid = new CubeGrid();
        Sprite wave = new Wave();
        mainProgressbar.setIndeterminateDrawable(doubleBounce);
        mainProgressbar.setVisibility(View.VISIBLE);

        databaseList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    StoreToDB upload = dataSnapshot1.getValue(StoreToDB.class);
                    databaseList.add(upload);
                }

                myAdapter = new BlogAdapter(getApplicationContext(), databaseList);

                recyclerView.setAdapter(myAdapter);

                mainProgressbar.setVisibility(View.INVISIBLE);
                searchEdittext.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                mainProgressbar.setVisibility(View.INVISIBLE);
            }

        });

        setDrawer();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ///For Search EditText
        searchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }



    //by me
    private void chkStatus() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting()) {
            //Toast.makeText(this, "Wifi", Toast.LENGTH_LONG).show();
        } else if (mobile.isConnectedOrConnecting()) {
            //Toast.makeText(this, "Mobile 3G ", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
            AlertDialog.Builder aleartDialogBuilder;
            aleartDialogBuilder = new AlertDialog.Builder(Main2Activity.this);

            aleartDialogBuilder.setTitle("Internet Connectivity !!!");
            aleartDialogBuilder.setMessage("Check Your Internet Connection And Try Again...");
            aleartDialogBuilder.setIcon(R.drawable.nointernet);
            aleartDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);//ofline databade use
            AlertDialog alertDialog = aleartDialogBuilder.create();
            alertDialog.show();
        }
    }

    //by me
    private void setDrawer() {
        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        nav_header_name = headerView.findViewById(R.id.nav_header_name);
        nav_header_email = headerView.findViewById(R.id.nav_header_email);
        nav_heder_image = headerView.findViewById(R.id.nav_header_imageView);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
        //profilePics = FirebaseDatabase.getInstance().getReference().child("Profile Pictures URL");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    StoreToDB_UsersInfo storeToDB_usersInfo = dataSnapshot1.getValue(StoreToDB_UsersInfo.class);

                    if (firebaseUser.getUid().equals(storeToDB_usersInfo.getUserID())) {
                        String iURL = storeToDB_usersInfo.getImageUrl();
                        //System.out.println("UserID == "+firebaseUser.getUid()+ "&&" +storeToDB_usersInfo.getUserID());

                        final String un = storeToDB_usersInfo.getUserName();
                        final String em = storeToDB_usersInfo.getUserEmail();

                        //Store SharedPre
                        SharedPreferences sharedPreferences = getSharedPreferences("myFileUNandEM", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("username", un);
                        editor.putString("email", em);
                        editor.putString("image", iURL);

                        editor.commit();

                        nav_header_name.setText(un);
                        nav_header_email.setText(em);

                        Picasso.with(getApplicationContext())
                                .load(iURL)
                                .placeholder(R.mipmap.ic_launcher)
                                .fit()
                                .centerCrop()
                                .into(nav_heder_image);

                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Main2Activity.this, "Somthing wents wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    //by me
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //FirebaseDatabase.getInstance().setPersistenceEnabled(true);//ofline databade use
                swipeRefreshLayout.setRefreshing(false);
                //onRefresh();
                Intent intent = new Intent(Main2Activity.this, Main2Activity.class);//refresh or restart current activity
                startActivity(intent);
                finish();
            }
        }, 1000);
        Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(Main2Activity.this, Post_Activity.class));
        } else if (item.getItemId() == R.id.action_about) {
            aleartDialogBuilder = new AlertDialog.Builder(Main2Activity.this);

            aleartDialogBuilder.setTitle("About The Application Creator");
            aleartDialogBuilder.setMessage("Bipro Bhowmik Joy\nLeading University(CSE)\n44th Batch\n7th Semester(Mar-2019)");
            aleartDialogBuilder.setIcon(R.drawable.alart_dialog_icon);
            aleartDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = aleartDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addpost) {
            // Handle the camera action
            startActivity(new Intent(Main2Activity.this, Post_Activity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(Main2Activity.this, AboutActivity.class));

        }else if (id == R.id.nav_myprofile) {

            if(firebaseUser.getUid() != null)
            {
                Intent passToProfile = new Intent(Main2Activity.this, MyProfileActivity.class);
                passToProfile.putExtra("UID", firebaseUser.getUid());
                startActivity(passToProfile);
            }

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Main2Activity.this, Login_activity.class));
            finish();

        } else if (id == R.id.nav_view_change) {

            if (viewChange == false) {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                viewChange = true;
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                viewChange = false;
            }

        } else if (id == R.id.nav_users) {

            startActivity(new Intent(Main2Activity.this, AllUsers.class));

        }else if (id == R.id.nav_groupMessage) {

            startActivity(new Intent(Main2Activity.this, MessageActivity.class));
        }

        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void filter(String text) {

        List<StoreToDB> filteredList = new ArrayList<>();

        for(StoreToDB item : databaseList)
        {
            if(item.getUserName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }

        myAdapter.filterListn(filteredList);
    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        //Toast.makeText(getApplicationContext(), "Exist", Toast.LENGTH_LONG).show();
//
//        DatabaseReference treference = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        treference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                {
//                    StoreToDB_UsersInfo storeToDB_usersInfo = dataSnapshot1.getValue(StoreToDB_UsersInfo.class);
//
//                    if(firebaseUser.getUid().equals(storeToDB_usersInfo.getUserID()))
//                    {
//                        DatabaseReference ch = FirebaseDatabase.getInstance().getReference().child("Users")
//                                .child(storeToDB_usersInfo.getKey()).child("onOffStatus");
//                        ch.setValue(false);
//
//                        break;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(Main2Activity.this, "Somthing wents wrong", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
}
