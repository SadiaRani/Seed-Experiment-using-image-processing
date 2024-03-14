package com.example.emotiondetection.Activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.emotiondetection.R;

public class HomeActivity extends AppCompatActivity {
    // Define the constant PICK_IMAGE_REQUEST here
//    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_PICK = 3;
    ImageView imageView;
    int imageSize = 224;
    private TextView result;
    private TextView confidence;
    ConstraintLayout row1;
    ConstraintLayout row2;
    ConstraintLayout row3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);

        row1 = findViewById(R.id.row1);
        row2 = findViewById(R.id.row2);
        row3 = findViewById(R.id.row3);

        checkPermissions();

        row3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Alert !")
                        .setMessage("Are you sure, you want to exit ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .show();
            }
        });
        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
////        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
////            Uri selectedImageUri = data.getData();
////            Bitmap image = (Bitmap) data.getExtras().get("data");
////            int dimension = Math.min(image.getWidth(),image.getHeight());
////            image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);
////            imageView.setImageBitmap(image);
////            image = Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
////            classifyImage(image);
//
//
//        // Set the selected image to the ImageView
////            ImageView imageView = findViewById(R.id.image_View);
////            imageView.setImageURI(selectedImageUri);
////            Uri imageUri = data.getData();
//
//
//
////        }
//
//
//        if (resultCode == RESULT_OK) {
//            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
////                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
////                Uri selectedImageUri = data.getData();
//                Bitmap image = (Bitmap) data.getExtras().get("data");
//                int dimension = Math.min(image.getWidth(),image.getHeight());
//                image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);
//                imageView.setImageBitmap(image);
//                image = Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
//                classifyImage(image);
//                // Use the captured image bitmap here
//                // ...
//            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
//                // Handle the selected gallery image here (e.g., get its URI or path)
//                // Convert the URI to a bitmap
//                try {
//                    Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                    int dimension = Math.min(image.getWidth(),image.getHeight());
//                    image = ThumbnailUtils.extractThumbnail(image,dimension,dimension);
//                    imageView.setImageBitmap(image);
//                    image = Bitmap.createScaledBitmap(image,imageSize,imageSize,false);
//                    classifyImage(image);
//                    // Use the selected image bitmap here
//                    // ...
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }



    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            // Show image source dialog if permissions are granted
//            showImageSourceDialog();
        }
    }



    private void openCamera() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } else {
//            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
//        }
    }

    private void openGallery() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK);
    }



}
