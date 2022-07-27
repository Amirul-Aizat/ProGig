package com.amirul.progig.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amirul.progig.Booking;
import com.amirul.progig.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.BookingViewHolder>{

    Context context;
    ArrayList<Booking> bookingsArrayList;
    FirebaseFirestore firebaseFirestore;
    private final OnItemClickListener listener;


    public BookingListAdapter(Context context, ArrayList<Booking> bookingArrayList, OnItemClickListener listener) {
        this.context=context;
        this.bookingsArrayList=bookingArrayList;
        this.listener= listener;
        firebaseFirestore=FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public BookingListAdapter.BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.booking_list,parent,false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingListAdapter.BookingViewHolder holder, int position) {

        Booking booking=bookingsArrayList.get(position);

        holder.bookingID.setText(booking.getBookingID());
        holder.booking_date.setText(booking.getBooking_date());
        holder.booking_time.setText(booking.getBooking_time());
        holder.service_name.setText(booking.getService_name());
        holder.gig_name.setText(booking.getGig_name());
        holder.location.setText(booking.getLocation());
        holder.status.setText(booking.getStatus());
        holder.bind(bookingsArrayList.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return bookingsArrayList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {


        TextView bookingID,location,service_name,booking_time,booking_date,gig_name,status;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);

            bookingID=itemView.findViewById(R.id.bookingID);
            service_name=itemView.findViewById(R.id.service_name);
            booking_time=itemView.findViewById(R.id.booking_time);
            booking_date=itemView.findViewById(R.id.booking_date);
            gig_name=itemView.findViewById(R.id.gig_name);
            location=itemView.findViewById(R.id.location);
            status=itemView.findViewById(R.id.status);


        }

        public void bind(final Booking booking, final BookingListAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(booking);
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Booking booking);
    }
}
