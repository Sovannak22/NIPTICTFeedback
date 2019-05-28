package com.example.niptictfeedback;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.niptictfeedback.apis.NewsApi;
import com.victor.loading.rotate.RotateLoading;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddNewsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Dialog dialog;
    private final int REQUEST_IMAGE_GALLERY = 2,REQUEST_IMAGE_CAPTURE=1;
    ImageView imageUploaded;
    EditText txtTitle,txtDescription;
    NewsApi newsApi;
    private final int STORAGE_PERMISSION_CODE=3;

    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    File f;

    RotateLoading rotateLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);


        toolbar = findViewById(R.id.toolbar_admin_news);
        setSupportActionBar(toolbar);

        dialog = new Dialog(this);
        imageUploaded = findViewById(R.id.image_upload);
        txtTitle = findViewById(R.id.txt_title);
        txtDescription = findViewById(R.id.txt_description);
        rotateLoading = findViewById(R.id.rotate_loading_addnews);
        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        newsApi = retrofit.create(NewsApi.class);
    }

//    sent post request to insert new to the server#################################################
    public void createNews(){
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        RequestBody titlePart = RequestBody.create(MultipartBody.FORM,title);
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,description);
        MultipartBody.Part body=null;
        if (f != null){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
            body = MultipartBody.Part.createFormData("image","fileAndroid", reqFile);
        }
        String auth=((MyApplication) getApplicationContext()).getAuthorization();
        Call call = newsApi.createNews(auth,body,titlePart,descriptionPart);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()){
                    Log.w("Register:: ",response.code()+""+response.message());
                    return;
                }

                Log.w("Register:: ","Successfully "+response);
                Intent intent = new Intent(AddNewsActivity.this,NewsAdminActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"News has added",Toast.LENGTH_LONG).show();
                rotateLoading.stop();
                finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.w("Register fail::",t.getMessage());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_news_admin_action_bar,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

    if (id == R.id.add_news){
            Toast.makeText(getApplicationContext(),"Add new clicked",Toast.LENGTH_LONG).show();
            rotateLoading.start();
            createNews();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showPopup(View v){
        dialog.setContentView(R.layout.uplaod_image_popup);
        LinearLayout btnTakePhot = dialog.findViewById(R.id.btn_take_photo);
        LinearLayout btnOpenCamera = dialog.findViewById(R.id.btn_open_gallery);
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (ContextCompat.checkSelfPermission(AddNewsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    iGallery.setType("image/*");
                    startActivityForResult(iGallery,REQUEST_IMAGE_GALLERY);
                }else {
                    requestPermission();
                }
            }
        });
        btnTakePhot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (iCamera.resolveActivity(getPackageManager()) != null){
                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_IMAGE_CAPTURE){
                Log.e("Upload image:: ","Camera");
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    Bitmap bitmapResize = Bitmap.createScaledBitmap(thumbnail,1000,750,true);
                    imageUploaded.setImageBitmap(thumbnail);
                    convertBitToBite(bitmapResize);
                    dialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                imageUploaded.setImageBitmap(bitmap);
            }
            else if (requestCode == REQUEST_IMAGE_GALLERY){
                Uri uri = data.getData();
                Bitmap bitmap = null;
                Log.e("Upload image:: ","Gallerry");
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    imageUploaded.setImageBitmap(bitmap);
                    Log.e("Upload image:: ","Gallerry");
                    Bitmap bitmapResize = Bitmap.createScaledBitmap(bitmap,1000,750,true);
                    convertBitToBite(bitmapResize);
                    dialog.dismiss();
                } catch (IOException e) {
                    Log.e("Error gallerry::","Error ");
                    e.printStackTrace();
                }
            }
        }
        else {
            Log.e("Upload image:: ",resultCode+"");
        }

    }

//    Request runtime permission android method
    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("We need to access your storage to get required data")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                iGallery.setType("image/*");
                startActivityForResult(iGallery,REQUEST_IMAGE_GALLERY);
            }else {
                Toast.makeText(this,"Permission DENIED",Toast.LENGTH_LONG).show();
            }
        }
    }


//    Convert from bitmap picture
    public void convertBitToBite(Bitmap bitmap) throws IOException {
        f = new File(getApplicationContext().getCacheDir(),"imageToUpload");
        f.createNewFile();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bitmapData=byteArrayOutputStream.toByteArray();

        FileOutputStream fileOutputStream = new FileOutputStream(f);
        fileOutputStream.write(bitmapData);
        fileOutputStream.flush();
        fileOutputStream.close();

    }
}

