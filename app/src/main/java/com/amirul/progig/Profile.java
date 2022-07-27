package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;
    BottomNavigationView bottom_navigation;
    TextView shareFriend, name, email;
    ImageView profileImage;
    String currentUID = user.getUid();
    TextView requestHired, requestCompleted, requestCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setSelectedItemId(R.id.profile);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        profileImage = findViewById(R.id.profileImage);


        getTotalHired();

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.task:
                        startActivity(new Intent(getApplicationContext(), Request.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        return true;
                }
                return false;
            }
        });

        readDB();

    }

    public void logOut(View view) {

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Profile.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    public void editProfile(View view) {

        Intent intent = new Intent(Profile.this, UserProfile.class);
        startActivity(intent);

    }

    public void readDB() {

        DocumentReference docRef = db.collection("customer").document("Customer " + currentUID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        name.setText(document.getData().get("name").toString());
                        email.setText(document.getData().get("email").toString());
                        String imageUri = document.getData().get("imageUri").toString();
                        Picasso.with(Profile.this).load(imageUri).into(profileImage);

                    } else {

                    }
                } else {
                    Toast.makeText(Profile.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void getTotalHired() {

        requestHired = findViewById(R.id.requestHired);
        requestCompleted = findViewById(R.id.requestCompleted);
        requestCancel = findViewById(R.id.requestCancel);

        db.collection("booking").whereEqualTo("customerID", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            int hired = 0, completed = 0, cancelled = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.getData().get("status").equals("Accepted")) {
                                    hired++;
                                } else if (document.getData().get("status").equals("Received")) {
                                    completed++;
                                } else if (document.getData().get("status").equals("Cancel")) {
                                    cancelled++;
                                }

                            }

                            requestHired.setText(hired + " request hired");
                            requestCompleted.setText(completed + " request completed");
                            requestCancel.setText(cancelled + " request cancelled");


                        } else {

                        }
                    }
                });

    }

}