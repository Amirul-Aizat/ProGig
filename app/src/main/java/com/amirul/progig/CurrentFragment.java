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


public class CurrentFragment extends Fragment {

    RecyclerView recyclerAccept,recyclePending;
    ArrayList<Booking> bookingArrayList,bookingArrayList2;
    BookingListAdapter bookingListAdapter,bookingListAdapter2;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Booking booking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_current, container, false);


        recyclerAccept = root.findViewById(R.id.recycleAccept);
        recyclerAccept.setHasFixedSize(true);
        recyclerAccept.setLayoutManager(new LinearLayoutManager(getActivity()));

        bookingArrayList = new ArrayList<Booking>();


        bookingListAdapter = new BookingListAdapter(getActivity(), bookingArrayList, new BookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Booking booking) {

                Intent intent=new Intent(getActivity(),ReceiptCurrent.class);
                intent.putExtra("Hold Booking",booking);
                startActivity(intent);
            }
        });
        recyclerAccept.setAdapter(bookingListAdapter);
        bookingListAdapter.notifyDataSetChanged();

        displayData();

        recyclePending = root.findViewById(R.id.recyclePending);
        recyclePending.setHasFixedSize(true);
        recyclePending.setLayoutManager(new LinearLayoutManager(getActivity()));

        bookingArrayList2 = new ArrayList<Booking>();


        bookingListAdapter2 = new BookingListAdapter(getActivity(), bookingArrayList2, new BookingListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Booking booking) {

                Intent intent2=new Intent(getActivity(),ReceiptPending.class);
                intent2.putExtra("Hold Booking",booking);
                startActivity(intent2);
            }
        });
        recyclePending.setAdapter(bookingListAdapter2);
        bookingListAdapter2.notifyDataSetChanged();

        displayData2();

        return root;
    }

    public void displayData() {

        db.collection("booking").whereEqualTo("customerID", currentUser.getUid()).whereEqualTo("status", "Accepted").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void displayData2(){

        db.collection("booking").whereEqualTo("customerID",currentUser.getUid()).whereEqualTo("status","Pending").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {


                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {

                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        bookingArrayList2.add(dc.getDocument().toObject(Booking.class));

                    }
                }
                bookingListAdapter2.notifyDataSetChanged();
            }

        });

    }
}