package com.example.emotiondetection.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emotiondetection.R;
import com.example.emotiondetection.ml.Model;

import org.jetbrains.annotations.Nullable;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
    TextView result, confidence;
    ImageView imageView;
    TextView picture ,classifyImageBtn,clearBtn,calculateParamsBtn;
    int imageSize = 224;   // which is given by our Trained Model
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_PICK = 3;

    boolean isImageSelected = false;
    Bitmap grayscaleImage;
    Bitmap parseBitMap;

    public static Bitmap getBitmap_transfer() {
        return bitmap_transfer;
    }

    public static void setBitmap_transfer(Bitmap bitmap_transfer) {
        MainActivity.bitmap_transfer = bitmap_transfer;
    }

    private static Bitmap bitmap_transfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        classifyImageBtn = findViewById(R.id.button_classify);
        clearBtn = findViewById(R.id.clear_btn);
        calculateParamsBtn = findViewById(R.id.params);

        picture.post(() -> picture.performClick());

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, 1);
                    showImageSourceDialog();

                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
                }
            }
        });

        classifyImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isImageSelected) {
                classifyImage(grayscaleImage);
                } else {
                    new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                            .setTitle("Alert !")
                            .setMessage("Please Select Image Befor Classification ?")
                            .setPositiveButton("OKay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing
                                }
                            })
                            .show();
                }
            }
        });

        calculateParamsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isImageSelected){
                    setBitmap_transfer(parseBitMap);

                    Intent intent = new Intent(MainActivity.this, ImageParaMeters.class);

                    startActivity(intent);
                } else {

                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageSelected = false;
                imageView.setImageDrawable(null);
                result.setText("");

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            // receive bitmap from data intent
            Bitmap image = (Bitmap) data.getExtras().get("data");
            // crop image

            int dimentions = Math.min(image.getWidth(),image.getHeight());
            imageView.setImageBitmap(image);
            parseBitMap = image;

            // create Image as a size of our model Requirement
            image = Bitmap.createScaledBitmap(image,imageSize, imageSize,false);
            // Resize the image to the desired input size

// Convert the resized image to grayscale
            grayscaleImage  = convertToGrayscale(image);

            classifyImage(grayscaleImage);



        } else if ( requestCode == 3) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                int dimentions = Math.min(image.getWidth(),image.getHeight());
                imageView.setImageBitmap(image);
                parseBitMap = image;

                // create Image as a size of our model Requirement
                image = Bitmap.createScaledBitmap(image,imageSize, imageSize,false);
                // Resize the image to the desired input size

// Convert the resized image to grayscale
                grayscaleImage = convertToGrayscale(image);
//                classifyImage(grayscaleImage);
                // Now you have the selected image as a Bitmap (selectedBitmap)
                // You can use the bitmap as needed
            } catch (IOException e) {
                e.printStackTrace();
        }
        }
    }

    private Bitmap convertToGrayscale(Bitmap originalBitmap) {
        Bitmap grayscaleBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayscaleBitmap);
        Paint paint = new Paint();
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0); // Convert to grayscale
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);
        canvas.drawBitmap(originalBitmap, 0, 0, paint);
        isImageSelected = true;
        return grayscaleBitmap;
    }

    private void classifyImage(Bitmap image) {
        System.out.println("====>> Classify 1 :");

        try {
            // Creates inputs for reference.
            System.out.println("====>> Classify 1a :");

            Model model = Model.newInstance(getApplicationContext());
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);


// Create the input buffer for the model
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }



            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            System.out.println("====>> Models Ouput :"+outputFeature0);
            System.out.println("====>> Models Ouput size  :"+outputFeature0.getFlatSize());

            // Manipulate OutPut Taked from Model

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
//            String[] classes ={"Angry  \uD83D\uDE21", "Disgust \uD83D\uDE2D", "Scared \uD83D\uDE31", "Happy \uD83D\uDE00", "Sad \uD83D\uDE41", "Surprised \uD83D\uDE32", "Neutral \uD83D\uDE10"};
            String[] classes ={"Healty Seeds", "Un Healty Seed","Undefined"};
            for (int i=0; i<confidences.length; i++){
                System.out.println("====>>"+classes[i]+":"+confidences[i]);
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }


            result.setText(classes[maxPos]);
            String s= "";


            confidence.setText(s);
            model.close();

        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    private void checkPermissionsAndShowDialog() {
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
            showImageSourceDialog();
        }
    }

    private void showImageSourceDialog() {
        isImageSelected = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    openCamera();
                } else {
                    openGallery();
                }
            }
        });
        builder.show();
    }


    private void openCamera() {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
    }

    private void openGallery() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK);
    }

}