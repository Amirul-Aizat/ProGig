package com.amirul.progig.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amirul.progig.R;
import com.amirul.progig.object.Service;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder> {

    Context context;
    ArrayList<Service> serviceArrayList;
    FirebaseFirestore firebaseFirestore;
    private final OnItemClickListener listener;

    public ServiceListAdapter(Context context,ArrayList<Service> serviceArrayList,OnItemClickListener listener) {
        this.context=context;
        this.serviceArrayList=serviceArrayList;
        this.listener=listener;
        firebaseFirestore=FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ServiceListAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.service_list,parent,false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListAdapter.ServiceViewHolder holder, int position) {

        Service service=serviceArrayList.get(position);
        holder.category.setText(service.getCategory());
        holder.service_name.setText(service.getService_name());
        holder.service_rate.setText(service.getServiceRate());
        holder.service_id.setText(service.getService_ID());
        holder.gig_name.setText(service.getGig_name());
        holder.location.setText(service.getService_area());
        holder.review.setRating(service.getReview());
        holder.bind(serviceArrayList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return serviceArrayList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        TextView category, service_name, service_rate,service_id,location,gig_name;
        RatingBar review;


        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            category = itemView.findViewById(R.id.category);
            service_name = itemView.findViewById(R.id.service_name);
            service_rate = itemView.findViewById(R.id.service_rate);
            service_id=itemView.findViewById(R.id.service_ID);
            location=itemView.findViewById(R.id.location);
            gig_name=itemView.findViewById(R.id.gig_name);
            review=itemView.findViewById(R.id.review);


        }

        public void bind(final Service service, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(service);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Service service);
    }
}

