package com.example.niptictfeedback;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.niptictfeedback.apis.FeedbackApi;
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

public class CreatePostActivity extends AppCompatActivity {

    private Intent intent;
    private TextView tvPlace;
    private Toolbar toolbar;
    private LinearLayout btnPrivacy,btnUploadPic,backgroundLoading;
    private FeedbackApi feedbackApi;
    private EditText txtTitle,txtDescription;
    private ImageView imageUploaded;
    private Dialog dialog;
    //Request code for select image or open camera dialog
    private final int REQUEST_IMAGE_GALLERY = 2,REQUEST_IMAGE_CAPTURE=1;
    private final int STORAGE_PERMISSION_CODE=3;

    private File f;

    private RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        intent = getIntent();
        tvPlace = findViewById(R.id.tv_place_create_post);
        toolbar = findViewById(R.id.tool_bar_create_post);
        btnPrivacy = findViewById(R.id.btn_privacy_create_post);
        btnUploadPic = findViewById(R.id.upload_pic_add_feedback);
        txtTitle = findViewById(R.id.txt_title_add_feedback);
        txtDescription = findViewById(R.id.txt_description_add_feedback);
        imageUploaded = findViewById(R.id.image_upload_add_feedback);
        backgroundLoading = findViewById(R.id.loading_background_add_feedback);
        backgroundLoading.setVisibility(View.GONE);
        rotateLoading = findViewById(R.id.rotate_loading_add_feedback);

        dialog = new Dialog(this);

        tvPlace.setText(intent.getStringExtra("Place"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatePostActivity.this,SelectPrivacyActivity.class);
                startActivity(intent);
            }
        });

        String baseUrl=((MyApplication) getApplicationContext()).getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        feedbackApi = retrofit.create(FeedbackApi.class);

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
            backgroundLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();
            createFeedback();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createFeedback(){
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        RequestBody titlePart = RequestBody.create(MultipartBody.FORM,title);
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,description);
        RequestBody placeIdPart = RequestBody.create(MultipartBody.FORM,intent.getStringExtra("PlaceId"));
        RequestBody feedbackTypeIdPart = RequestBody.create(MultipartBody.FORM,"1");
        MultipartBody.Part body=null;
        if (f != null){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
            body = MultipartBody.Part.createFormData("img","fileAndroid", reqFile);
        }
        String auth=((MyApplication) getApplicationContext()).getAuthorization();
        Call call = feedbackApi.createFeedback(auth,body,titlePart,descriptionPart,placeIdPart,feedbackTypeIdPart);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()){
                    Log.w("Register:: ",response.code()+""+response.message());
                    rotateLoading.stop();
                    backgroundLoading.setVisibility(View.GONE);
                    return;
                }

                Log.w("Register:: ","Successfully "+response);
                Intent intent = new Intent(CreatePostActivity.this,AppActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"News has added",Toast.LENGTH_LONG).show();
                rotateLoading.stop();
                finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.w("Register fail::",t.getMessage());
                rotateLoading.stop();
                backgroundLoading.setVisibility(View.GONE);
            }
        });
    }

    //TODO:Popup show to chose select image from gallery or take photo by camera
    public void showPopup(View v){
        dialog.setContentView(R.layout.uplaod_image_popup);
        LinearLayout btnTakePhot = dialog.findViewById(R.id.btn_take_photo);
        LinearLayout btnOpenCamera = dialog.findViewById(R.id.btn_open_gallery);
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (ContextCompat.checkSelfPermission(CreatePostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
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
                    startActivityForResult(iCamera,REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        dialog.show();
    }

    //TODO:: Request permission to access external storage
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

    //TODO: After get data from select image or take picture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_IMAGE_CAPTURE){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imageUploaded.setImageBitmap(bitmap);
                Log.e("Upload image:: ","Camera");
                try {
                    convertBitToBite(bitmap);
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

    //TODO::    Convert from bitmap picture to bite array
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
