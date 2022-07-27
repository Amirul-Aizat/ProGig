package com.amirul.progig;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;

import com.amirul.progig.adapter.ServiceListAdapter;
import com.amirul.progig.object.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchGig extends AppCompatActivity {


    ArrayList<Service> servicesArrayList;
    RecyclerView recyclerService;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ServiceListAdapter serviceListAdapter;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    SearchView searchView;
    ArrayList<Service> search1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_gig);

        searchView=findViewById(R.id.search);
        recyclerService=findViewById(R.id.recycleService);
        recyclerService.setHasFixedSize(true);
        recyclerService.setLayoutManager(new LinearLayoutManager(this));
        db=FirebaseFirestore.getInstance();

        servicesArrayList=new ArrayList<Service>();

        serviceListAdapter = new ServiceListAdapter(SearchGig.this, servicesArrayList, new ServiceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Service service) {
                Intent intent=new Intent(SearchGig.this,GigReview.class);
                intent.putExtra("Hold Service", service);
                startActivity(intent);
            }
        });

        recyclerService.setAdapter(serviceListAdapter);
        serviceListAdapter.notifyDataSetChanged();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                search1 = new ArrayList<Service>();

                String word = "";

                for (int i = 0; i < newText.length(); i++) {
                    if (i == 0) {
                        word += Character.toUpperCase(newText.charAt(i));
                    } else if (newText.charAt(i - 1) == ',') {
                        word += Character.toUpperCase(newText.charAt(i));
                    } else {
                        word += Character.toLowerCase(newText.charAt(i));
                    }
                }

                for (Service service : servicesArrayList) {
                    if (service.getService_name().contains(word)) {
                        if (!search1.contains(service)) {
                            search1.add(service);
                        }
                    }

                    if (service.getGig_name().contains(word)) {
                        if (!search1.contains(service)) {
                            search1.add(service);
                        }
                    }


                }


                serviceListAdapter = new ServiceListAdapter(SearchGig.this, search1, new ServiceListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Service service) {
                        Intent intent = new Intent(SearchGig.this, GigReview.class);
                        intent.putExtra("Hold Service", service);
                        startActivity(intent);
                    }
                });
                recyclerService.setAdapter(serviceListAdapter);
                serviceListAdapter.notifyDataSetChanged();
                return false;
            }
        });
        recyclerService.setAdapter(serviceListAdapter);

        displayData();

    }

    public void displayData(){

        db.collection("service").addSnapshotListener(new EventListener<QuerySnapshot>() {
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