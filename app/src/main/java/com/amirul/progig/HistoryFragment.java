package com.amirul.progig;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amirul.progig.adapter.BookingListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    RecyclerView recyclerHistory;
    ArrayList<Booking> bookingArrayList;
    BookingListAdapter bookingListAdapter;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_history, container, false);

        recyclerHistory = root.findViewById(R.id.recycleHistory);
        recyclerHistory.setHasFixedSize(true);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(getActivity()));

        bookingArrayList = new ArrayList<Booking>();


        bookingListAdapter = new BookingListAdapter(getActivity(), bookingArrayList, new BookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Booking booking) {
                Intent intent2=new Intent(getActivity(),ReceiptHistory.class);
                intent2.putExtra("Hold Booking",booking);
                startActivity(intent2);
            }
        });
        recyclerHistory.setAdapter(bookingListAdapter);
        bookingListAdapter.notifyDataSetChanged();

        displayData();

        return root;
    }

    public void displayData(){

        db.collection("booking").whereEqualTo("customerID",currentUser.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
}