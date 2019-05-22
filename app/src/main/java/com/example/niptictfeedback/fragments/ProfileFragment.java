package com.example.niptictfeedback.fragments;

import android.Manifest;
import android.app.Dialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.niptictfeedback.AddNewsActivity;
import com.example.niptictfeedback.EditProfileInforActivity;
import com.example.niptictfeedback.R;
import com.example.niptictfeedback.adapter.page_adapter.PageAdapter;
import com.example.niptictfeedback.adapter.page_adapter.ProfilePageAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {
    }

    TabLayout tabLayout;
    TabItem publicTabItem,privateTabItem,responseTabItem;
    ViewPager viewPager;
    ProfilePageAdapter adapter;
    ImageView edit_img,cameraProfile;
    Dialog dialog;
    private final int STORAGE_PERMISSION_CODE=3;
    private final int REQUEST_IMAGE_GALLERY = 2,REQUEST_IMAGE_CAPTURE=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);


        tabLayout = v.findViewById(R.id.tabLayout_id);
        publicTabItem = v.findViewById(R.id.tab_public);
        privateTabItem = v.findViewById(R.id.tab_private);
        responseTabItem = v.findViewById(R.id.tab_response);
        viewPager = v.findViewById(R.id.viewPager_id);
        edit_img = v.findViewById(R.id.edit_infor);
        cameraProfile = v.findViewById(R.id.camera_profile);

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

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        return v;

    }

//    --------Upload Profile picture--------

    public void showPopup(View v){
        dialog.setContentView(R.layout.uplaod_image_popup);
        LinearLayout btnTakePhot = dialog.findViewById(R.id.btn_take_photo);
        LinearLayout btnOpenCamera = dialog.findViewById(R.id.btn_open_gallery);
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
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
                    startActivityForResult(iCamera,REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        dialog.show();
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
}
