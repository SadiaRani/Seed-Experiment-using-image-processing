package com.example.emotiondetection.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emotiondetection.MyApplication;
import com.example.emotiondetection.R;

public class ImageParaMeters extends AppCompatActivity {
    TextView t1,t2,t3,t4,t5,t6,t7,t8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_params);
        t1 = findViewById(R.id.v1);
        t2 = findViewById(R.id.v2);
        t3 = findViewById(R.id.v3);
        t4 = findViewById(R.id.v4);
        t5 = findViewById(R.id.v5);
        t6 = findViewById(R.id.v6);
        t7 = findViewById(R.id.v7);
        t8 = findViewById(R.id.v8);


        Bitmap bitmap = MainActivity.getBitmap_transfer();


            if(bitmap !=null){

// Dimensions
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

// Size
                int byteCount = bitmap.getByteCount(); // Total bytes in the bitmap
                int allocationByteCount = bitmap.getAllocationByteCount(); // Allocated memory size

// Configuration
                Bitmap.Config config = bitmap.getConfig();

// Memory Usage
                int memoryUsage = bitmap.getRowBytes() * height; // Approximate memory used by the bitmap

// Pixel Information
                int pixelColor = bitmap.getPixel(width-1, height-1);

// Color Space and Channels
                int colorChannels = 4; // Assuming ARGB configuration


                // Set Values
                t1.setText(""+width);
                t2.setText(""+height);
                t3.setText(""+byteCount);
                t4.setText(""+allocationByteCount);
                t5.setText(""+config);
                t6.setText(""+memoryUsage);
                t7.setText(""+pixelColor);
                t8.setText(""+colorChannels);


            }







    }
}
