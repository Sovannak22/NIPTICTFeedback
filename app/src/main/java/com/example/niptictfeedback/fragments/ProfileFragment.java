package com.example.niptictfeedback.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niptictfeedback.AddNewsActivity;
import com.example.niptictfeedback.EditProfileInforActivity;
import com.example.niptictfeedback.MyApplication;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.adapter.page_adapter.PageAdapter;
import com.example.niptictfeedback.adapter.page_adapter.ProfilePageAdapter;
import com.example.niptictfeedback.apis.UserApi;
import com.example.niptictfeedback.models.User;
import com.example.niptictfeedback.sqlite.UserDBHelper;
import com.squareup.picasso.Picasso;

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

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {
    }

    MenuItem item;
    TabLayout tabLayout;
    TabItem publicTabItem,privateTabItem,responseTabItem;
    ViewPager viewPager;
    ProfilePageAdapter adapter;
    ImageView edit_img,cameraProfile;
    TextView tvProfileName;
    Dialog dialog;
    UserApi userApi;
    UserDBHelper userDBHelper;
    private final int STORAGE_PERMISSION_CODE=3;
    private final int REQUEST_IMAGE_GALLERY = 2,REQUEST_IMAGE_CAPTURE=1;
    User user;

    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    File f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        setHasOptionsMenu(true);

        tabLayout = v.findViewById(R.id.tabLayout_id);
        publicTabItem = v.findViewById(R.id.tab_public);
        privateTabItem = v.findViewById(R.id.tab_private);
        responseTabItem = v.findViewById(R.id.tab_response);
        viewPager = v.findViewById(R.id.viewPager_id);
        edit_img = v.findViewById(R.id.edit_infor);
        cameraProfile = v.findViewById(R.id.profile_image);
        tvProfileName = v.findViewById(R.id.tvProfile_name);

        //--Call shoPopup  function
        cameraProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        dialog = new Dialog(getContext());

        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileInforActivity.class);
                startActivity(intent);
            }
        });


        adapter = new ProfilePageAdapter(getActivity(), getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        userDBHelper = new UserDBHelper(getContext());
        userDBHelper.createNewTable();
        user = userDBHelper.getLoginUser();
        Log.w("Profile URL::",user.getProfileImg());
       // Picasso.get().load(baseUrl+(user.getProfileImg())).into(cameraProfile);
        tvProfileName.setText(user.getName()+"");

        String baseUrl=((MyApplication)getActivity().getApplication()).getBaseUrl();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        userApi = retrofit.create(UserApi.class);
        return v;

    }

    public void changeProfile(){
        String auth=((MyApplication)getActivity().getApplication()).getAuthorization();
        MultipartBody.Part body=null;

        RequestBody methodPart = RequestBody.create(MultipartBody.FORM,"PATCH");

        if (f != null){
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
            body = MultipartBody.Part.createFormData("image","fileAndroid", reqFile);
        }

        Call<User> call = userApi.updateProfilePicture(auth,body,methodPart);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Log.w("Upload Profile:: ",response.code()+""+response.message());
                    return;
                }
                User userResponce = response.body();
                userDBHelper.updateProfilePic(userResponce.getProfileImg());
                Log.w("Upload Profile:: ","Successfully "+response);
                item.setVisible(false);
                Toast.makeText(getContext(),"Update Profile successfully",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w("Upload Profile fail::","Fail "+t.getMessage());
            }
        });
    }

//    --------Upload Profile picture--------

    public void showPopup(View v){
        dialog.setContentView(R.layout.uplaod_image_popup);
        LinearLayout btnTakePhot = dialog.findViewById(R.id.btn_take_photo);
        LinearLayout btnOpenCamera = dialog.findViewById(R.id.btn_open_gallery);
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                if (iCamera.resolveActivity(getActivity().getPackageManager()) != null){
                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = getActivity().getContentResolver().insert(
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_IMAGE_CAPTURE){
                Log.e("Upload image:: ","Camera");
                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(
                            getActivity().getContentResolver(), imageUri);
                    Bitmap bitmapResize = Bitmap.createScaledBitmap(thumbnail,1000,750,true);
                    cameraProfile.setImageBitmap(thumbnail);
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
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                    cameraProfile.setImageBitmap(bitmap);
                    Log.e("Upload image:: ","Gallerry");
                    Bitmap bitmapResize = Bitmap.createScaledBitmap(bitmap,500,350,true);
                    convertBitToBite(bitmapResize);
                    dialog.dismiss();
                } catch (IOException e) {
                    Log.e("Error gallerry::","Error ");
                    e.printStackTrace();
                }
            }
            item.setVisible(true);
        }
        else {
            Log.e("Upload image:: ",resultCode+"");
        }

    }

    //    Request runtime permission android method
    private void requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(getContext())
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
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
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
                Toast.makeText(getContext(),"Permission DENIED",Toast.LENGTH_LONG).show();
            }
        }
    }

    //    Convert from bitmap picture
    public void convertBitToBite(Bitmap bitmap) throws IOException {
        f = new File(getContext().getCacheDir(),"imageToUpload");
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.add_news_admin_action_bar,menu);
        item = menu.findItem(R.id.add_news);
        item.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_news){
            changeProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
