package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReceiptCurrent extends AppCompatActivity {

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    Booking booking;
    Button receive;
    TextView bookingID,service_name,gig_name,status,date,time,message, name,name1,phone,address,gigID,gig_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_current);

        booking = (Booking) getIntent().getSerializableExtra("Hold Booking");


        bookingID=findViewById(R.id.bookingID);
        service_name=findViewById(R.id.service_name);
        gig_name=findViewById(R.id.gig_name);
        status=findViewById(R.id.status);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        message=findViewById(R.id.message);
        name=findViewById(R.id.name);
        name1=findViewById(R.id.name1);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        receive=findViewById(R.id.btnReceived);
        gigID=findViewById(R.id.gig_id);
        gig_phone=findViewById(R.id.gig_phone);


        DocumentReference docRef = db.collection("progig").document("ProGig "+gigID.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        gig_phone.setText(document.getData().get("phone").toString());


                    } else {

                    }
                } else {
                    Toast.makeText(ReceiptCurrent.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });



        displayBookingDetails(booking);
        displayInfo();

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference documentReference = db.collection("booking").document(booking.getBookingID());

                documentReference
                        .update("status", "Received")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent=new Intent(ReceiptCurrent.this,Request.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


            }
        });


    }

    public void displayBookingDetails(Booking booking){

        DocumentReference docRef = db.collection("booking").document(booking.getBookingID());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        bookingID.setText(document.getData().get("bookingID").toString());
                        service_name.setText(document.getData().get("service_name").toString());
                        gig_name.setText(document.getData().get("gig_name").toString());
                        status.setText(document.getData().get("status").toString());
                        date.setText(document.getData().get("booking_date").toString());
                        time.setText(document.getData().get("booking_time").toString());
                        message.setText(document.getData().get("message").toString());
                        gigID.setText(document.getData().get("gigID").toString());

                    } else {

                    }
                } else {
                    Toast.makeText(ReceiptCurrent.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void displayInfo(){

        DocumentReference docRef = db.collection("customer").document("Customer " + user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        name.setText(document.getData().get("name").toString());
                        phone.setText(document.getData().get("phone").toString());
                        address.setText(document.getData().get("address").toString());
                        name1.setText(document.getData().get("name").toString());

                    } else {

                    }
                } else {
                    Toast.makeText(ReceiptCurrent.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openWs(View view){

        String url = "https://api.whatsapp.com/send?phone="+"+6"+gig_phone.getText().toString();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

}