package com.joy.thejsblog;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUp_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password, username, confirnPassword;
    private Button signup;
    private TextView signin;
    private ImageView faceImage;
    private ProgressBar progressBar;

    private static final int gallaryRequest = 1;
    private Uri imageUri;

    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference reference;

    //private String imgURL;
    private String userId;
    private boolean imageStored = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Profile Pictures URL");

        email = findViewById(R.id.editTextEmailSignup);
        password = findViewById(R.id.editTextPasswordSignup);
        confirnPassword = findViewById(R.id.editTextConfirmPasswordSignup);
        username = findViewById(R.id.editTextUsernameSignup);
        signup = findViewById(R.id.buttonSignup);
        signin = findViewById(R.id.textViewSignin);
        faceImage = findViewById(R.id.imageViewProfileImage);
        progressBar = findViewById(R.id.progressBar);

        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
        faceImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSignup) {

            final String emailString = email.getText().toString().trim();
            final String passwordString = password.getText().toString().trim();
            final String confirmPasswordString = confirnPassword.getText().toString().trim();
            final String usernameString = username.getText().toString().trim();

            boolean emailVarification = validationEmail(emailString);
            boolean passwordVarification = validationPassword(passwordString);

            if (TextUtils.isEmpty(usernameString)) {
                username.setError("Enter a Username");
                username.requestFocus();
            } else if (!emailVarification) {
                email.setError("Enter a valid Emali");
                email.requestFocus();
            } else if (!passwordVarification) {
                password.setError("Enter a >=6 length Password");
                password.requestFocus();
            } else if (imageUri == null) {
                Toast.makeText(getApplicationContext(), "Give a Profile Picture", Toast.LENGTH_LONG).show();
            }
            else if (!passwordString.equals(confirmPasswordString))
            {
                confirnPassword.setError("Password didn't match");
                confirnPassword.requestFocus();
            }
            else {
                imageStored = true;
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(emailString, passwordString)//pass email pass
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    imageStored = true;

                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    userId = firebaseUser.getUid();
                                    reference = FirebaseDatabase.getInstance().getReference("Users");

                                    Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Not Registered", Toast.LENGTH_LONG).show();
                                    imageStored = false;
                                }


                            }
                        });

                if(imageStored == true)
                {
                    ///Image Store Start
                    StorageReference filePath = mStorage.child("Profile Picture").child(imageUri.getLastPathSegment()/*return image name*/);

                    filePath.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //String imgURL = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(); // it returns maybe device path

                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl(); // image url
                                    while (!uriTask.isSuccessful());
                                    Uri downloadUrl = uriTask.getResult();
                                    String imgURL = downloadUrl.toString();

//                                    String key = mDatabase.push().getKey();
//                                    mDatabase.child(key).setValue(new ForProfilePic(imgURL, emailString));
                                    String key = reference.push().getKey();

                                    StoreToDB_UsersInfo storeToDB_usersInfo = new StoreToDB_UsersInfo(userId, usernameString, emailString,
                                            passwordString, imgURL, key, true);

                                    reference.child(key).setValue(storeToDB_usersInfo);

                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "PP uploaded", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(SignUp_Activity.this, Main2Activity.class);
                                    //intent.putExtra("URL", imgURL);
                                    startActivity(intent);
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                    Toast.makeText(getApplicationContext(), "Not Posted", Toast.LENGTH_LONG).show();
                                    //mProgress.dismiss();
                                }
                            });
                    ///Image store end
                }

            }


        } else if (v.getId() == R.id.textViewSignin) {
            Intent intent = new Intent(this, Login_activity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.imageViewProfileImage) {
            Intent galarryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galarryIntent.setType("image/*");
            startActivityForResult(galarryIntent, gallaryRequest);
        }
    }

    private boolean validationPassword(String passwordString) {
        if (passwordString.isEmpty() || passwordString.length() < 6) {
            return false;
        }

        return true;
    }

    private boolean validationEmail(String emailString) {
        if (emailString.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gallaryRequest && resultCode == RESULT_OK) {
            imageUri = data.getData();
            faceImage.setImageURI(imageUri);
        }
    }

}