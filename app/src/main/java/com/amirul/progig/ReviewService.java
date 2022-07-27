package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReviewService extends AppCompatActivity {

    TextView serviceID,name;
    RatingBar ratingBar;
    TextInputLayout comment;
    Button submit;
    String service_id;
    Booking booking;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String date = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_service);

        booking = (Booking) getIntent().getSerializableExtra("Hold Booking");

        serviceID=findViewById(R.id.service_id);
        ratingBar=findViewById(R.id.review);
        submit=findViewById(R.id.submit);
        comment=findViewById(R.id.comment);
        name=findViewById(R.id.customer_name);

        readData(booking);
        readData2();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(comment.getEditText().getText().toString().isEmpty()){
                    comment.setError("Please leave a comment");
                }
                else{
                    Map<String, Object> review = new HashMap<>();
                    review.put("serviceID",serviceID.getText().toString());
                    review.put("rating",ratingBar.getRating());
                    review.put("comment",comment.getEditText().getText().toString());
                    review.put("customerID", user.getUid());
                    review.put("customerName",name.getText().toString());
                    review.put("date",date);

                    db.collection("review").add(review).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            documentReference.update("reviewID", documentReference.getId());
                            Log.e("TAG", documentReference.getId());
                            Intent intent = new Intent(ReviewService.this, Request.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

            }
        });

    }

    public void readData(Booking booking){

        DocumentReference docRef = db.collection("booking").document(booking.getBookingID());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        serviceID.setText(document.getData().get("serviceID").toString());

                    } else {

                    }
                } else {
                    Toast.makeText(ReviewService.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void readData2(){

        DocumentReference docRef = db.collection("customer").document("Customer "+user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        name.setText(document.getData().get("name").toString());

                    } else {

                    }
                } else {
                    Toast.makeText(ReviewService.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}