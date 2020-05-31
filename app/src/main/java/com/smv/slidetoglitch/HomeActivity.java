package com.smv.slidetoglitch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class HomeActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQ_CODE = 1001;
    private static final int STORAGE_PERMISSION_REQ_CODE = 1002;
    private String mCapturedImagePath;
    private static final String FILE_PROVIDER_AUTHORITY = "com.smv.photoedit";
    private static final int GALLERY_RESULT = 1;
    private static final int CAMERA_RESULT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }

    public void openCamera(View view) {
        if(ContextCompat.checkSelfPermission(this,CAMERA)!= PERMISSION_GRANTED){
            String[] cameraPermission = { CAMERA };
            ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_PERMISSION_REQ_CODE);
        }else {
            dispatchImageCaptureIntent();
        }
    }

    private void dispatchImageCaptureIntent(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager())!=null){
            File photo = null;
            try {
                photo = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photo != null) {
                Uri photoFileUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, photo);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFileUri);
                startActivityForResult(cameraIntent, CAMERA_RESULT);
            }
        }
    }
    private File createImageFile()throws IOException {
        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCapturedImagePath = image.getAbsolutePath();
        return image;
    }

    public void openGallery(View view){
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            String[] storagePermissions = { READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE };
            ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_PERMISSION_REQ_CODE);
        } else {
            dispatchGalleryIntent();
        }
    }
    private void dispatchGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_RESULT);
    }


    private Bundle uriToBundle(Uri imageUri) {
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.IMAGE_URI, imageUri.toString());
        return bundle;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_RESULT) {
                Uri imageUri = data.getData();
                startActivity(MainActivity.getIntent(this, uriToBundle(Objects.requireNonNull(imageUri))));
            } else if (requestCode == CAMERA_RESULT) {
                    File imageFile = new File(mCapturedImagePath);
                    Uri imageUri = Uri.fromFile(imageFile);
                    startActivity(MainActivity.getIntent(this, uriToBundle(imageUri)));
            }
        } else {
            Toast.makeText(this, "Image not loaded.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQ_CODE:
                if (grantResults[0] == PERMISSION_GRANTED) {
                    dispatchImageCaptureIntent();
                } else {
                    Toast.makeText(this, "Required camera permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;

            case STORAGE_PERMISSION_REQ_CODE:
                if (grantResults[0] == PERMISSION_GRANTED) {
                    dispatchGalleryIntent();
                } else {
                    Toast.makeText(this, "Required storage permission not granted", Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            default:
                throw new IllegalArgumentException("Unexpected request code");
        }
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }


}
