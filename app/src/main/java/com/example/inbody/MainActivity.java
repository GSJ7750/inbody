package com.example.inbody;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    ConstraintLayout lo_iv1;
    ConstraintLayout lo_iv2;
    String[] permission_list = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    boolean pic1 = false;
    boolean pic2 = false;
    boolean isEnable = false;
    Button btn_save;


    int T_PIC1 = 1;
    int T_PIC2 = 2;
    int G_PIC1 = 4;
    int G_PIC2 = 5;
    int CROP_PIC = 3;

    Uri picUri;
    int num;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        this.InitializeView();
        this.setListner();
    }
    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됬다면
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                }
                else {
                    Toast.makeText(getApplicationContext(),"앱권한설정하세요",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
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

        btn_save = (Button)findViewById(R.id.btn_save);

        iv1 = (ImageView)findViewById(R.id.imageView1);
        iv2 = (ImageView)findViewById(R.id.imageView2);

        lo_btns1 = (LinearLayout)findViewById(R.id.lo_btns1);
        lo_btns2 = (LinearLayout)findViewById(R.id.lo_btns2);
        lo_iv1 = (ConstraintLayout)findViewById(R.id.lo_iv1);
        lo_iv2 = (ConstraintLayout)findViewById(R.id.lo_iv2);



    }
    public void setListner(){
        View.OnClickListener Listner = new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View v) {
                Intent i;
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
                        i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, T_PIC1);
                        break;
                    case R.id.btn_camera2:
                        i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, T_PIC2);
                        break;
                    case R.id.btn_album1:
                        i = new Intent(Intent.ACTION_PICK);
                        i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(i, G_PIC1);

                        break;
                    case R.id.btn_album2:
                        i = new Intent(Intent.ACTION_PICK);
                        i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(i, G_PIC2);
                        break;
                    case R.id.btn_save:
                        BitmapDrawable bitmapDrawable1 = (BitmapDrawable)iv1.getDrawable();
                        BitmapDrawable bitmapDrawable2 = (BitmapDrawable)iv2.getDrawable();
                        Bitmap bitmap1 = bitmapDrawable1.getBitmap();
                        Bitmap bitmap2 = bitmapDrawable2.getBitmap();
                        savePic(bitmap1, bitmap2, String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
                        Log.d("saved",String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
                        Toast.makeText(getApplicationContext(), "사진을 저장중입니다 ", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        btn_add1.setOnClickListener(Listner);
        btn_add2.setOnClickListener(Listner);
        btn_camera1.setOnClickListener(Listner);
        btn_camera2.setOnClickListener(Listner);
        btn_album1.setOnClickListener(Listner);
        btn_album2.setOnClickListener(Listner);
        btn_save.setOnClickListener(Listner);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            if(requestCode == T_PIC1){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                picUri = getImageUri(this, imageBitmap);
                num = 1;
                if(imageBitmap != null){
                    pic1 = true;
                }
                performCrop();

            }else if(requestCode == T_PIC2){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                picUri = getImageUri(this, imageBitmap);
                num = 2;
                if(imageBitmap != null) {
                    pic2 = true;
                }
                performCrop();

            }else if(requestCode == CROP_PIC){
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.getParcelable("data");
                if(num == 1){
                    iv1.setImageBitmap(image);
                    setGone(new LinearLayout[] {lo_btns1});
                    setVisible(new ConstraintLayout[] {lo_iv1});
                }else{
                    iv2.setImageBitmap(image);
                    setGone(new LinearLayout[] {lo_btns2});
                    setVisible(new ConstraintLayout[] {lo_iv2});
                }
                if(pic1 && pic2){
                    isEnable = true;
                    btn_save.setEnabled(true);
                }
            }else if(requestCode == G_PIC1){
                try {
                    picUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(picUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    iv1.setImageBitmap(selectedImage);
                    setGone(new LinearLayout[] {lo_btns1});
                    setVisible(new ConstraintLayout[] {lo_iv1});
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if(requestCode == G_PIC2){
                try {
                    picUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(picUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    iv2.setImageBitmap(selectedImage);
                    setGone(new LinearLayout[] {lo_btns2});
                    setVisible(new ConstraintLayout[] {lo_iv2});
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 9);
            cropIntent.putExtra("aspectY", 21);
            cropIntent.putExtra("outputX", 180);
            cropIntent.putExtra("outputY", 420);
            cropIntent.putExtra("return-data", true);
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

    public void savePic(Bitmap c, Bitmap s,String loc) {
        Bitmap cs = null;

        int width, height = 0;

        if(c.getWidth() > s.getWidth()) {

            height = c.getHeight();
            width = c.getWidth()+s.getWidth();
        } else {
            height = s.getHeight();
            width = c.getWidth()+s.getWidth();
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);

        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, c.getWidth(), 0f, null);


        String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

        OutputStream os = null;
        try {
            os = new FileOutputStream(loc + tmpImg);
            cs.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch(IOException e) {
            Log.e("combineImages", "problem combining images", e);
        }

    }

}