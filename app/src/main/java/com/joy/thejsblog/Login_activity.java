package com.joy.thejsblog;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_activity extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password, userName;
    private Button signin;
    private TextView signup;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        signin = findViewById(R.id.buttonSignin);
        signup = findViewById(R.id.textViewSignup);
        progressBar = findViewById(R.id.progressBarMain);

        Sprite doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            startActivity(new Intent(Login_activity.this, Main2Activity.class));
            finish();
        }
        //updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSignin) {

            String emailString = email.getText().toString().trim();
            String passwordString = password.getText().toString().trim();

            boolean emailVarification = validationEmail(emailString);
            boolean passwordVarification = validationPassword(passwordString);

            if (!emailVarification) {
                email.setError("Enter a valid Emali");
                email.requestFocus();
            }
            if (!passwordVarification) {
                password.setError("Enter a <=6 length Password");
                password.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(emailString, passwordString)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Authentication Successfulled",
                                            Toast.LENGTH_SHORT).show();

                                    progressBar.setVisibility(View.GONE);

                                    Intent intent = new Intent(Login_activity.this, Main2Activity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);



                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });

            }

        } else if (v.getId() == R.id.textViewSignup) {
            Intent intent = new Intent(this, SignUp_Activity.class);
            startActivity(intent);
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
}
