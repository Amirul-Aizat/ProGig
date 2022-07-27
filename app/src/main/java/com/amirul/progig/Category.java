package com.amirul.progig;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amirul.progig.adapter.ServiceListAdapter;
import com.amirul.progig.object.Service;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Category extends AppCompatActivity {

    TextView category;
    RecyclerView recyclerView;
    String category1;
    RadioGroup radio_filter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ServiceListAdapter serviceListAdapter;
    ArrayList<Service> servicesArrayList;
    ArrayList<Service> search1=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            category1=getIntent().getStringExtra("Hold Category");
        }

//        radio_filter=findViewById(R.id.radio_filter);
        category=findViewById(R.id.category);
        recyclerView=findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db=FirebaseFirestore.getInstance();

        category.setText(category1);

        servicesArrayList=new ArrayList<Service>();

        serviceListAdapter = new ServiceListAdapter(Category.this, servicesArrayList, new ServiceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Service service) {
                Intent intent=new Intent(Category.this,GigReview.class);
                intent.putExtra("Hold Service", service);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(serviceListAdapter);
        serviceListAdapter.notifyDataSetChanged();

        displayData(category1);

//        radio_filter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                ArrayList<Service> filter=new ArrayList<>();
//
//                for(Service service:search1){
//                    if(service.getService_area().contains(((RadioButton)findViewById(checkedId)).getText())){
//                        filter.add(service);
//                    }
//                }
//
//                serviceListAdapter = new ServiceListAdapter(Category.this, filter, new ServiceListAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Service service) {
//                        Intent intent = new Intent(Category.this, RequestBooking.class);
//                        intent.putExtra("Hold Service", service);
//                        startActivity(intent);
//                    }
//                });
//                recyclerView.setAdapter(serviceListAdapter);
//                serviceListAdapter.notifyDataSetChanged();
//
//
//            }
//        });


    }

    public void displayData(String a){

        db.collection("service").whereEqualTo("category",a).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.e("error", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        servicesArrayList.add(dc.getDocument().toObject(Service.class));

                    }
                }
                serviceListAdapter.notifyDataSetChanged();
            }

        });

    }
}