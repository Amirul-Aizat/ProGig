package com.amirul.progig.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amirul.progig.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.ImageHolder> {

    int[] images;

    public SliderAdapter(int[]images){
        this.images=images;
    }


    @Override
    public SliderAdapter.ImageHolder onCreateViewHolder(ViewGroup parent) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_image,parent,false);

        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.ImageHolder viewHolder, int position) {
        viewHolder.imageView.setImageResource(images[position]);

    }

    @Override
    public int getCount() {
        return images.length;
    }

    public class ImageHolder extends SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public ImageHolder(View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.poster_view);
        }
    }
}
