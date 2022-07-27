package com.amirul.progig;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amirul.progig.adapter.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class ChooseActivity extends AppCompatActivity {

    SliderView image_slide;
    int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3};
    Button btnNext;
    TextView btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        image_slide=findViewById(R.id.image_slide);
        btnNext=findViewById(R.id.btnNext);
        btnSignUp=findViewById(R.id.btnSignUp);

        SliderAdapter slideAdapter = new SliderAdapter(images);
        image_slide.setSliderAdapter(slideAdapter);
        image_slide.setIndicatorAnimation(IndicatorAnimationType.WORM);
        image_slide.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        image_slide.startAutoCycle();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseActivity.this,Login.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChooseActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
    }
}