package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amirul.progig.adapter.BookingListAdapter;
import com.amirul.progig.adapter.ReviewListAdapter;
import com.amirul.progig.object.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GigReview extends AppCompatActivity {

    Service service;
    TextView gig_name,service_id,category,service_name,service_rate,location,description;
    RatingBar review;
    String serviceID;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    Button hire;
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView recyclerReview;
    ArrayList<Review> reviewArrayList;
    ReviewListAdapter reviewListAdapter;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_review);

        service = (Service) getIntent().getSerializableExtra("Hold Service");

        gig_name=findViewById(R.id.gig_name);
        service_id=findViewById(R.id.service_id);
        category=findViewById(R.id.category);
        service_name=findViewById(R.id.service_name);
        review=findViewById(R.id.review);
        service_rate=findViewById(R.id.service_rate);
        location=findViewById(R.id.location);
        description=findViewById(R.id.description);
        hire=findViewById(R.id.hire);
        recyclerReview=findViewById(R.id.recycleReview);
        imageView=findViewById(R.id.service_image);

        serviceID=service.getService_ID();
        displayService(serviceID);


        recyclerReview.setHasFixedSize(true);
        recyclerReview.setLayoutManager(new LinearLayoutManager(GigReview.this));

        reviewArrayList = new ArrayList<Review>();


        reviewListAdapter = new ReviewListAdapter(GigReview.this, reviewArrayList, new ReviewListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Review review) {

            }
        });
        recyclerReview.setAdapter(reviewListAdapter);
        reviewListAdapter.notifyDataSetChanged();

        displayData();


        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GigReview.this,RequestBooking.class);
                intent.putExtra("Hold ID",serviceID);
                startActivity(intent);
            }
        });

    }

    public void  displayService(String a){

        DocumentReference docRef = db.collection("service").document(a);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        gig_name.setText(document.getData().get("gig_name").toString());
                        service_id.setText(document.getData().get("service_ID").toString());
                        category.setText(document.getData().get("category").toString());
                        review.setRating(Float.parseFloat(document.getData().get("review").toString()));
                        service_name.setText(document.getData().get("service_name").toString());
                        service_rate.setText(document.getData().get("serviceRate").toString());
                        location.setText(document.getData().get("service_area").toString());
                        description.setText(document.getData().get("description").toString());
                        String imageUri = document.getData().get("Service Image").toString();
                        Picasso.with(GigReview.this).load(imageUri).into(imageView);

                    } else {

                    }
                } else {
                    Toast.makeText(GigReview.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void displayData() {

        db.collection("review").whereEqualTo("serviceID", serviceID).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {


                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        reviewArrayList.add(dc.getDocument().toObject(Review.class));

                    }
                }
                reviewListAdapter.notifyDataSetChanged();
            }

        });

    }

}