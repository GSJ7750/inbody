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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private Button btn_add1;
    private Button btn_add2;

    private Button btn_camera1;
    private Button btn_camera2;
    private final Button[] btns1 = new Button[2];

    private Button btn_album1;
    private Button btn_album2;
    private final Button[] btns2 = new Button[2];

    private ImageView iv1;
    private ImageView iv2;

    private LinearLayout lo_btns1;
    private LinearLayout lo_btns2;
    private ConstraintLayout lo_iv1;
    private ConstraintLayout lo_iv2;
    private final String[] permission_list = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private boolean pic1 = false;
    private boolean pic2 = false;
    private Button btn_save;


    private final int T_PIC1 = 1;
    private final int T_PIC2 = 2;
    private final int G_PIC1 = 4;
    private final int G_PIC2 = 5;
    private final int CROP_PIC = 3;

    private Uri picUri;
    private int num;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        this.InitializeView();
        this.setListner();
    }
    private void checkPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
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
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                }
                else {
                    Toast.makeText(getApplicationContext(),"권한 오류",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }



    private void InitializeView()
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
    private void setListner(){
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
                if(imageBitmap != null){
                    num = 1;
                    pic1 = true;
                }
                performCrop();
            }else if(requestCode == T_PIC2){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                picUri = getImageUri(this, imageBitmap);
                if(imageBitmap != null) {
                    num = 2;
                    pic2 = true;
                }
                performCrop();
            }else if(requestCode == G_PIC1){
                try {
                    picUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(picUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    if(selectedImage != null){
                        num = 1;
                        pic1 = true;
                    }
                    performCrop();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if(requestCode == G_PIC2){
                try {
                    picUri = data.getData();
                    InputStream imageStream = getContentResolver().openInputStream(picUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    if(selectedImage != null){
                        num = 2;
                        pic2 = true;
                    }
                    performCrop();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
                    btn_save.setEnabled(true);
                }
            }
        }
    }
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void performCrop() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 9);
            cropIntent.putExtra("aspectY", 21);
            cropIntent.putExtra("outputX", 180);
            cropIntent.putExtra("outputY", 420);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, CROP_PIC);
        }catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void setGone(View[] vs) {
        for (View v : vs) {
            v.setVisibility(View.GONE);
        }
    }
    private void setVisible(View[] vs){
        for (View v : vs) {
            v.setVisibility(View.VISIBLE);
        }
    }
    private static boolean fileDelete(String filePath){
        try {
            File file = new File(filePath);

            if(file.exists()) {
                file.delete();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    private void savePic(Bitmap c, Bitmap s,String loc) {
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

        String tmpImg = "/BeforeNAfter_0.png";
        OutputStream os = null;
        fileDelete(loc+tmpImg);
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