package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amirul.progig.object.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class SignUp extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button signUp;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextInputLayout email, password, confirmPassword;
    User user = new User();
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInButton btnGoogle;
    TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUp = findViewById(R.id.btnSignUp);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnLogin=findViewById(R.id.btnLogin);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,Login.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email.setError(null);
                password.setError(null);
                confirmPassword.setError(null);

                if (email.getEditText().getText().toString().isEmpty() || password.getEditText().getText().toString().isEmpty() || confirmPassword.getEditText().getText().toString().isEmpty()) {
                    confirmPassword.setError("Please fill all the details");
                } else {
                    if (isValidEmail(email.getEditText().getText().toString())) {
                        if (password.getEditText().getText().toString().length() < 8) {
                            password.setError("The password must be at least 8 character");
                        } else {
                            if (password.getEditText().getText().toString().equals(confirmPassword.getEditText().getText().toString())) {
                                createUserEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString());
                            } else {
                                confirmPassword.setError("The password are not same");
                            }
                        }
                    } else {
                        email.setError("The email is not valid");
                    }
                }


            }
        });
    }

    public boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public void createUserEmailAndPassword(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        user.setUid(authResult.getUser().getUid());
                        user.setComplete(false);
                        user.setEmail(email);
                        uploadCustomerToDB(user);
                        Intent intent = new Intent(SignUp.this, FillProfile.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void uploadCustomerToDB(User user) {

        db.collection("customer").document("Customer " + user.getUid()).set(user).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.print("Fail to Upload");
            }
        });

        db.collection("progig").document("ProGig " + user.getUid()).set(user).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.print("Fail to Upload");
            }
        });
    }

    public void signIn() {
        Intent intent=gsc.getSignInIntent();
        startActivityForResult(intent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                goToDashboard();
            } catch (ApiException e){
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }


    }
    void goToDashboard(){

        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);


        user.setUid(account.getId());
        user.setComplete(false);
        user.setEmail(account.getEmail());
        uploadCustomerToDB(user);
        finish();
        Intent intent = new Intent(SignUp.this,FillProfile.class);
        startActivity(intent);
        finish();
    }
}

