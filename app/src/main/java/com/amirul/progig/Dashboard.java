package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.amirul.progig.adapter.BookingListAdapter;
import com.amirul.progig.adapter.SliderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    SliderView image_slide;
    int[] images = {R.drawable.postercust, R.drawable.postercust2, R.drawable.postercust3};
    LinearLayout search;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;
    String  currentUser= user.getUid();
    RecyclerView recyclerView;
    ArrayList<Booking> bookingArrayList;
    BookingListAdapter bookingListAdapter;
    String date = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        image_slide=findViewById(R.id.image_slide);
        bottom_navigation=findViewById(R.id.bottom_navigation);
        bottom_navigation.setSelectedItemId(R.id.home);

        SliderAdapter slideAdapter = new SliderAdapter(images);
        image_slide.setSliderAdapter(slideAdapter);
        image_slide.setIndicatorAnimation(IndicatorAnimationType.WORM);
        image_slide.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        image_slide.startAutoCycle();

        search=findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,SearchGig.class);
                startActivity(intent);
            }
        });

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        return true;

                    case R.id.task:
                        startActivity(new Intent(getApplicationContext(),Request.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recycleRequest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        bookingArrayList = new ArrayList<Booking>();


        bookingListAdapter = new BookingListAdapter(this, bookingArrayList, new BookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Booking booking) {
                Intent intent=new Intent(Dashboard.this,ReceiptCurrent.class);
                intent.putExtra("Hold Booking",booking);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(bookingListAdapter);
        bookingListAdapter.notifyDataSetChanged();

        displayData();

    }

    public void displayData() {

        db.collection("booking").whereEqualTo("customerID", currentUser).whereEqualTo("status", "Accepted").whereEqualTo("booking_date", date).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {


                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        bookingArrayList.add(dc.getDocument().toObject(Booking.class));

                    }
                }
                bookingListAdapter.notifyDataSetChanged();
            }

        });
    }

    public void goCategory1(View view){

        Intent intent=new Intent(Dashboard.this,Category.class);
        intent.putExtra("Hold Category","Family Essential");
        startActivity(intent);

    }

    public void goCategory2(View view){

        Intent intent=new Intent(Dashboard.this,Category.class);
        intent.putExtra("Hold Category","Cooking");
        startActivity(intent);

    }

    public void goCategory3(View view){

        Intent intent=new Intent(Dashboard.this,Category.class);
        intent.putExtra("Hold Category","Home Improvement");
        startActivity(intent);

    }

    public void goCategory4(View view){

        Intent intent=new Intent(Dashboard.this,Category.class);
        intent.putExtra("Hold Category","Cleaning Service");
        startActivity(intent);

    }

    public void goCategory5(View view){

        Intent intent=new Intent(Dashboard.this,Category.class);
        intent.putExtra("Hold Category","Family Essential");
        startActivity(intent);

    }

    public void goCategory6(View view){

        Intent intent=new Intent(Dashboard.this,Category.class);
        intent.putExtra("Hold Category","Transport and Errands");
        startActivity(intent);

    }
    public void goCategory7(View view){

        Intent intent=new Intent(Dashboard.this,Category.class);
        intent.putExtra("Hold Category","Creative and Content");
        startActivity(intent);

    }

    public void goCategory8(View view){

        Intent intent=new Intent(Dashboard.this,Category.class);
        intent.putExtra("Hold Category","Tutoring");
        startActivity(intent);

    }

}