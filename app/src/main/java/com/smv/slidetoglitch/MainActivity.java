package com.smv.slidetoglitch;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.smv.slidetoglitch.Filter.Glitcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    public static final String IMAGE_URI = "IMAGE_URI_KEY";
    private ImageView imageView;
    private Bitmap originalBitmap;
    private Bitmap glitchBitmap;
    Uri imageUri;

    private int amount=24;
    private int seed=53;
    private int iterations=21;
    private int quality = 69;

    private SeekBar amountSeek;
    private SeekBar seedSeek;
    private SeekBar iterationsSeek;
    private SeekBar qualitySeek;
    private TextView amountText;
    private TextView seedText;
    private TextView iterationsText;
    private TextView qualityText;
    private Button originalPreview;

    private LinearLayout glitchLayout;
    private LinearLayout aboutLayout;
    private Button glitchButton;
    private Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageUri = Uri.parse(getIntent().getExtras().getString(IMAGE_URI));
            try {
                InputStream in =  getContentResolver().openInputStream(imageUri);
                originalBitmap = decodeFile(in);
                imageView.setImageBitmap(originalBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        amountText = findViewById(R.id.amountValue);
        seedText = findViewById(R.id.seedValue);
        iterationsText = findViewById(R.id.iterationsValue);
        qualityText = findViewById(R.id.qualityValue);

        amountSeek = findViewById(R.id.amountSeek);
        seedSeek = findViewById(R.id.seedSeek);
        iterationsSeek = findViewById(R.id.iterationsSeek);
        qualitySeek = findViewById(R.id.qualitySeek);

        amountSeek.setOnSeekBarChangeListener(this);
        seedSeek.setOnSeekBarChangeListener(this);
        iterationsSeek.setOnSeekBarChangeListener(this);
        qualitySeek.setOnSeekBarChangeListener(this);

        glitchLayout = findViewById(R.id.glitchLayout);
        aboutLayout = findViewById(R.id.aboutLayout);
        glitchButton = findViewById(R.id.Glitch);
        aboutButton = findViewById(R.id.About);
        glitchLayout.setVisibility(View.VISIBLE);
        aboutLayout.setVisibility(View.INVISIBLE);

        originalPreview = findViewById(R.id.originalPreview);
        originalPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    imageView.setImageBitmap(originalBitmap);
                }else{
                    imageView.setImageBitmap(glitchBitmap);
                }
                return true;
            }
        });

        initGlitch();
    }

    private Bitmap decodeFile(InputStream in) {
        Bitmap b = null;
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int IMAGE_MAX_SIZE = 1000;
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = getContentResolver().openInputStream(imageUri);
            b = BitmapFactory.decodeStream(in, null, o2);
            in.close();


        }catch (Exception e){
            e.printStackTrace();
        }
        return b;
    }

    public void initGlitch(){
        glitchBitmap = Glitcher.glitch(originalBitmap,amount,seed,iterations,quality);
        imageView.setImageBitmap(glitchBitmap);
        amountText.setText(""+amount);
        amountSeek.setProgress(amount);
        seedText.setText(""+seed);
        seedSeek.setProgress(seed);
        iterationsText.setText(""+iterations);
        iterationsSeek.setProgress(iterations);
        qualityText.setText(""+quality);
        qualitySeek.setProgress(quality);
    }


    public void aboutVisible(View view) {
        glitchLayout.setVisibility(View.INVISIBLE);
        aboutLayout.setVisibility(View.VISIBLE);
    }

    public void glitchVisible(View view) {
        glitchLayout.setVisibility(View.VISIBLE);
        aboutLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.amountSeek:
                amount = progress;
                glitchBitmap = Glitcher.glitch(originalBitmap, amount, seed, iterations, quality);
                imageView.setImageBitmap(glitchBitmap);
                amountText.setText("" + progress);
                break;
            case R.id.seedSeek:
                seed = progress;
                glitchBitmap = Glitcher.glitch(originalBitmap, amount, seed, iterations, quality);
                imageView.setImageBitmap(glitchBitmap);
                seedText.setText("" + progress);
                break;
            case R.id.iterationsSeek:
                iterations = progress;
                glitchBitmap = Glitcher.glitch(originalBitmap, amount, seed, iterations, quality);
                imageView.setImageBitmap(glitchBitmap);
                iterationsText.setText("" + progress);
                break;
            case R.id.qualitySeek:
                if (progress == 0) {
                    quality = 1;
                    glitchBitmap = Glitcher.glitch(originalBitmap, amount, seed, iterations, quality);
                    imageView.setImageBitmap(glitchBitmap);
                    qualityText.setText("" + 1);
                } else {
                    quality = progress;
                    glitchBitmap = Glitcher.glitch(originalBitmap, amount, seed, iterations, quality);
                    imageView.setImageBitmap(glitchBitmap);
                    qualityText.setText("" + progress);
                }
                break;
        }
        }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public static Intent getIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MainActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        return intent;
    }

    public void saveImage(View view) {
        String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
        String imageFileName = "SToG_" + timeStamp + ".jpeg";
        File direct = new File(Environment.getExternalStorageDirectory()+"/SToG");
        if(!direct.exists()){
            File imageDirect = new File("/storage/emulated/0/SToG/");
            imageDirect.mkdir();
        }
        File file = new File("/storage/emulated/0/SToG/",imageFileName);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(file);
            glitchBitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();
            Toast.makeText(this, "Image Saved at SToG/", Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void backToHome(View view) {
        finish();
    }
}
