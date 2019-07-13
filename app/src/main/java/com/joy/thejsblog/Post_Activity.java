package com.joy.thejsblog;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Post_Activity extends AppCompatActivity implements View.OnClickListener {

    private Animation animation; //animation

    private ImageButton imageButton;
    private EditText title, discription;
    private Button submitButton;

    private static final int gallaryRequest = 1;
    private Uri imageUri;

    private StorageReference mStorage;
    private DatabaseReference mDatabase, loveReference;
    private FirebaseUser user;
    private ProgressDialog mProgress;

    private boolean netConnection = false;

    private static final int CHANNEL_ID = 1009;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_);

        //Animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.simple_animation);


        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        loveReference = FirebaseDatabase.getInstance().getReference().child("Love Reacts");
        //mProgress = new ProgressDialog(this);

        //back button
        getSupportActionBar().setTitle("Post Your Idea");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageButton = findViewById(R.id.imageButton);
        title = findViewById(R.id.titleID);
        discription = findViewById(R.id.decID);
        submitButton = findViewById(R.id.submitButton);

        imageButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.imageButton)
        {
            Intent galarryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galarryIntent.setType("image/*");
            startActivityForResult(galarryIntent, gallaryRequest);
        }
        else if(v.getId() == R.id.submitButton)
        {
            submitButton.startAnimation(animation);

            final String title_val = title.getText().toString().trim();
            final String dicription_val = discription.getText().toString().trim();

            chkStatus();

            if(!title_val.isEmpty() && !dicription_val.isEmpty() && imageUri != null && netConnection){
                //mProgress.setMessage("Posting To Blog ... ");
                mProgress = ProgressDialog.show(this, "The J's Blog", "Posting To Blog ... ");

                StorageReference filePath = mStorage.child("Blog").child(imageUri.getLastPathSegment()/*return image name*/);

                filePath.putFile(imageUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                title.setText(null);
                                discription.setText(null);

                                Toast.makeText(getApplicationContext(), "Posted Successfully", Toast.LENGTH_LONG).show();
                                mProgress.dismiss();
                                // Get a URL to the uploaded content
                                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                                while (!uriTask.isSuccessful());

                                Uri downloadUrl = uriTask.getResult();

                                String key = mDatabase.push().getKey();
                                //String keyy = loveReference.push().getKey();
                                String uName = "", eMail = "";

                                //Load SharedPre
                                SharedPreferences sharedPreferences = getSharedPreferences("myFileUNandEM", Context.MODE_PRIVATE);
                                if (sharedPreferences.contains("username") && sharedPreferences.contains("email")){

                                    uName = sharedPreferences.getString("username", "Unknown");
                                    eMail = sharedPreferences.getString("email", "Unknown");
                                }

                                LocalDate date = LocalDate.now();
                                LocalTime time = LocalTime.now();

                                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("E, MMM dd yyy");
                                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");

                                mDatabase.child(key).setValue(new StoreToDB(title_val, dicription_val,
                                        downloadUrl.toString(), uName, eMail, key, date.format(dateFormatter),
                                        time.format(timeFormatter), sharedPreferences.getString("image", "url")));

                                loveReference.child(key).child(key+uName).setValue(new Love("no", key, 0));
                                startActivity(new Intent(Post_Activity.this, Main2Activity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                System.out.println("Exception : " + exception);
                                Toast.makeText(getApplicationContext(), "Not Posted", Toast.LENGTH_LONG).show();
                                mProgress.dismiss();
                            }
                        });

            }
            else {
                Toast.makeText(getApplicationContext(), "Choose Image, Title and Discription", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void chkStatus() {
        final ConnectivityManager connMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting () || mobile.isConnectedOrConnecting ()) {
            netConnection = true;
            //Toast.makeText(this, "Wifi", Toast.LENGTH_LONG).show();
        }
//        else if (mobile.isConnectedOrConnecting ()) {
//            //Toast.makeText(this, "Mobile 3G ", Toast.LENGTH_LONG).show();
//        }
        else {
            netConnection = false;
            //Toast.makeText(this, "No Network ", Toast.LENGTH_LONG).show();
            AlertDialog.Builder aleartDialogBuilder;
            aleartDialogBuilder = new AlertDialog.Builder(Post_Activity.this);

            aleartDialogBuilder.setTitle("Internet Connectivity !!!");
            aleartDialogBuilder.setMessage("Check Your Internet Connection And Try Again...");
            aleartDialogBuilder.setIcon(R.drawable.nointernet);
            aleartDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = aleartDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == gallaryRequest && resultCode == RESULT_OK)
        {
            imageUri = data.getData();
            imageButton.setImageURI(imageUri);
        }
    }

}
