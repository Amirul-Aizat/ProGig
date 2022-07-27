package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseUser currentUser=mAuth.getCurrentUser();
                if (currentUser !=null){
                    DocumentReference reference=db.collection("customer").document("Customer "+currentUser.getUid());
                    reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document=task.getResult();

                                if(document.get("complete").toString().equals("false")){

                                    Intent intent =new Intent(MainActivity.this,FillProfile.class);
                                    startActivity(intent);

                                }else{

                                    Intent intent =new Intent(MainActivity.this,Dashboard.class);
                                    startActivity(intent);
                                }
                            }else{
                                System.out.print("Failed");
                            }
                        }
                    });

                } else if(currentUser==null) {
                    Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
                    startActivity(intent);
                }

            }
        },4000);
    }
}