package com.example.niptictfeedback;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class EditPostActivity extends AppCompatActivity {

    private Intent intent;
    private TextView tvPlace,tvPrivacy;
    private Toolbar toolbar;
    private LinearLayout btnPrivacy,btnUploadPic,backgroundLoading;
    private FeedbackApi feedbackApi;
    private EditText txtDescription;
    private ImageView imageUploaded,privacyIcon;
    private Dialog dialog;
    private String privacy="1",privacyString="public";
    private final int REQUEST_PRIVACY_CODE=4;
    private final int REQUEST_IMAGE_GALLERY = 2,REQUEST_IMAGE_CAPTURE=1;
    private final int STORAGE_PERMISSION_CODE=3;

    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;

    private File f;

    private RotateLoading rotateLoading;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        intent = getIntent();
        tvPlace = findViewById(R.id.tv_place_edit_post);
        toolbar = findViewById(R.id.tool_bar_edit_post);
        btnPrivacy = findViewById(R.id.btn_privacy_edit_post);
        btnUploadPic = findViewById(R.id.upload_pic_edit_post);
        txtDescription = findViewById(R.id.txt_description_edit_post);
        imageUploaded = findViewById(R.id.image_upload_edit_post);
        backgroundLoading = findViewById(R.id.loading_background_edit_post);
        backgroundLoading.setVisibility(View.GONE);
        rotateLoading = findViewById(R.id.rotate_loading_edit_post);
        tvPrivacy = findViewById(R.id.privacy_text_edit_post);
        privacyIcon = findViewById(R.id.privacy_icon_post);

        dialog = new Dialog(this);

        String placId = intent.getStringExtra("PlaceID");
        txtDescription.setText(intent.getStringExtra("Description"));

        if (placId.equals("1")){
            tvPlace.setText("Dorm");
        }else {
            tvPlace.setText("Canteen");
        }

        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditPostActivity.this,SelectPrivacyActivity.class);
                startActivityForResult(intent,REQUEST_PRIVACY_CODE);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
//            Toast.makeText(getApplicationContext(),"Add new clicked",Toast.LENGTH_LONG).show();
            backgroundLoading.setVisibility(View.VISIBLE);
            rotateLoading.start();
            editFeedback();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void editFeedback(){
        String description = txtDescription.getText().toString();
        RequestBody descriptionPart = RequestBody.create(MultipartBody.FORM,description);
        RequestBody feedbackTypeIdPart = RequestBody.create(MultipartBody.FORM,privacy);
        RequestBody methodPart = RequestBody.create(MultipartBody.FORM,"PUT");
        MultipartBody.Part body=null;
        if (f != null){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
            body = MultipartBody.Part.createFormData("img","fileAndroid", reqFile);
        }
        String auth=((MyApplication) getApplicationContext()).getAuthorization();
        Call call = feedbackApi.editFeedback(auth,intent.getStringExtra("FeedbackID"),body,descriptionPart,feedbackTypeIdPart,methodPart);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful()){
                    Log.w("EditFeedback:: ",response.code()+""+response.message());
                    rotateLoading.stop();
                    backgroundLoading.setVisibility(View.GONE);
                    return;
                }

                Log.w("EditFeedback:: ","Successfully "+response);
                Intent intent = new Intent(EditPostActivity.this,AppActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Feedback has been edited",Toast.LENGTH_LONG).show();
                rotateLoading.stop();
                finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.w("Edit feedback fail::",t.getMessage()+"");
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
                if (ContextCompat.checkSelfPermission(EditPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
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
//                    startActivityForResult(iCamera,REQUEST_IMAGE_CAPTURE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            //TODO: After get data from select image or take picture
            if (requestCode == REQUEST_IMAGE_CAPTURE){
                Log.e("Upload image:: ","Camera");

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);
                        Bitmap bitmapResize = Bitmap.createScaledBitmap(thumbnail,1000,750,true);
                        imageUploaded.setImageBitmap(thumbnail);
                        convertBitToBite(bitmapResize);
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
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
        //TODO: Wait result from acitivity select privacy
        else if (requestCode==REQUEST_PRIVACY_CODE){
            privacy = data.getStringExtra("Privacy");
            privacyString = data.getStringExtra("PrivacyString");
            tvPrivacy.setText(privacyString);

            if (privacy.equals("1")){
                privacyIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_public_24dp));
            }else {
                privacyIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_address));
            }
        }
        else {
            Log.e("Upload image:: ",resultCode+"");
        }

    }

    //TODO::Convert from bitmap picture to bite array
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
