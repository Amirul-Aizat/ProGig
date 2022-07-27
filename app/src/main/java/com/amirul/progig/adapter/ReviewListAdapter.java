package com.amirul.progig.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.amirul.progig.Booking;
import com.amirul.progig.R;
import com.amirul.progig.Review;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ReviewViewHolder>{

    Context context;
    ArrayList<Review> reviewArrayList;
    FirebaseFirestore firebaseFirestore;
    private final ReviewListAdapter.OnItemClickListener listener;


    public ReviewListAdapter(Context context, ArrayList<Review> reviewArrayList, ReviewListAdapter.OnItemClickListener listener) {
        this.context=context;
        this.reviewArrayList=reviewArrayList;
        this.listener= listener;
        firebaseFirestore=FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public ReviewListAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.review_list,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.ReviewViewHolder holder, int position) {

        Review review=reviewArrayList.get(position);

        holder.customerName.setText(review.getCustomerName());
        holder.date.setText(review.getDate());
        holder.comment.setText(review.getComment());
        holder.rating.setRating(review.getRating());
        holder.bind(reviewArrayList.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {


        TextView customerName,date,comment;
        RatingBar rating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            customerName=itemView.findViewById(R.id.customer_name);
            date=itemView.findViewById(R.id.date);
            comment=itemView.findViewById(R.id.comment);
            rating=itemView.findViewById(R.id.review);



        }

        public void bind(final Review review, final ReviewListAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(review);
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(Review review);
    }

}
