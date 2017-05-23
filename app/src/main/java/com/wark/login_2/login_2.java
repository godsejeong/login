package com.wark.login_2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class login_2 extends AppCompatActivity {
    Button button;
    ImageView imageview;
    private static File imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

    Uri fileUri;
    String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_2);
        button = (Button) findViewById(R.id.camera);
        imageview = (ImageView) findViewById(R.id.icon);


        button.setOnClickListener(new View.OnClickListener() {


                @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFileUri();
                takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);

                startActivityForResult(takePictureIntent, 100);


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) { //카메라를 실제로 찍었는지, 취소로 나갔는지


                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);

                    ExifInterface ei = new ExifInterface(path);
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);

                    Bitmap resultBitmap;
                    switch(orientation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            resultBitmap = rotateImage(bitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            resultBitmap = rotateImage(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            resultBitmap = rotateImage(bitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:

                        default:
                            resultBitmap = bitmap;
                            break;
                    }

                    imageview.setImageBitmap(resultBitmap);

                    //		selPhoto = Images.Media.getBitmap( getContentResolver(), imgUri );

                } catch (OutOfMemoryError | IOException e) {

                    // TODO Auto-generated catch block

                    e.printStackTrace();

                }


            }

        }
    }

    private Uri getOutputMediaFileUri() {
        // check for external storage
        try {

            if (isExternalStorageAvailable()) {
                // Create an image file nameFile imagePath = new File(Context.getFilesDir(), "images");
                String imagePath = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File storageDir = new File(imageDirectory, "Camera");
                File image = File.createTempFile(imagePath, ".jpg", storageDir);
                path = image.getAbsolutePath();
                return FileProvider.getUriForFile(this, "com.example.android.fileprovider", image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}

