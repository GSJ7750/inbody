package com.example.inbody;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_add1;
    Button btn_add2;

    Button btn_camera1;
    Button btn_camera2;
    Button btns1[] = new Button[2];

    Button btn_album1;
    Button btn_album2;
    Button btns2[] = new Button[2];

    ImageView iv1;
    ImageView iv2;

    LinearLayout lo_btns1;
    LinearLayout lo_btns2;
    LinearLayout lo_iv1;
    LinearLayout lo_iv2;
    final int CROP_PIC = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.InitializeView();
        this.setListner();
    }
    public void InitializeView()
    {
        btn_add1 = (Button)findViewById(R.id.btn_add1);
        btn_add2 = (Button)findViewById(R.id.btn_add2);
        setVisible(new Button[] {btn_add1, btn_add2});

        btn_camera1 = (Button)findViewById(R.id.btn_camera1);
        btn_album1 = (Button)findViewById(R.id.btn_album1);
        btns1[0] = btn_camera1;
        btns1[1] = btn_album1;

        btn_camera2 = (Button)findViewById(R.id.btn_camera2);
        btn_album2 = (Button)findViewById(R.id.btn_album2);
        btns2[0] = btn_camera2;
        btns2[1] = btn_album2;

        iv1 = (ImageView)findViewById(R.id.imageView1);
        iv2 = (ImageView)findViewById(R.id.imageView2);

        lo_btns1 = (LinearLayout)findViewById(R.id.lo_btns1);
        lo_btns2 = (LinearLayout)findViewById(R.id.lo_btns2);
        lo_iv1 = (LinearLayout)findViewById(R.id.lo_iv1);
        lo_iv2 = (LinearLayout)findViewById(R.id.lo_iv2);



    }
    public void setListner(){
        View.OnClickListener Listner = new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_add1:
                        setGone(new Button[] {btn_add1});
                        setVisible(btns1);
                        break;

                    case R.id.btn_add2:
                        setGone(new Button[] {btn_add2});
                        setVisible(btns2);
                        break;

                    case R.id.btn_camera1:
                        Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i1, 1);
                        setGone(new LinearLayout[] {lo_btns1});
                        setVisible(new LinearLayout[] {lo_iv1});
                        break;
                    case R.id.btn_camera2:
                        Intent i2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i2, 2);
                        setGone(new LinearLayout[] {lo_btns2});
                        setVisible(new LinearLayout[] {lo_iv2});
                        break;
                    case R.id.btn_album1:
                        break;
                    case R.id.btn_album2:
                        break;
                }
            }
        };
        btn_add1.setOnClickListener(Listner);
        btn_add2.setOnClickListener(Listner);
        btn_camera1.setOnClickListener(Listner);
        btn_camera2.setOnClickListener(Listner);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            performCrop(data);

            Bundle extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");

            if(requestCode == 1){
                iv1.setImageBitmap(image);
            }else if(requestCode == 2){
                iv2.setImageBitmap(image);
            }

        }
    }
    private void performCrop(Intent data) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void setGone(View[] vs) {
        for (View v : vs) {
            v.setVisibility(View.GONE);
        }
    }
    public void setVisible(View[] vs){
        for (View v : vs) {
            v.setVisibility(View.VISIBLE);
        }
    }
    public void addPic(){

    }

}