package com.example.dellpc.start;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Photo extends Fragment {


    private static final int RC_PHOTO_PICKER = 2 ;
    private Button photoPicker;
    private ImageView myImage;
    private TextView skip;

    private View view;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseRefrence;

    private ChildEventListener mChildEventListner;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotoStorageReference;

    private int check = 0;


    public Photo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo, container, false);
            //  mFirebaseDatabase.setPersistenceEnabled(true);

            mFirebaseDatabase = mFirebaseDatabase.getInstance();
            mFirebaseAuth = mFirebaseAuth.getInstance();
            mFirebaseStorage = mFirebaseStorage.getInstance();


            mMessageDatabaseRefrence = mFirebaseDatabase.getReference().child("User");
            mChatPhotoStorageReference = mFirebaseStorage.getReference().child("user_photos");

        myImage = (ImageView)view.findViewById(R.id.UserImage);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/JPEG");
                getIntent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                startActivityForResult(Intent.createChooser(getIntent,"Complete Action Using:"), RC_PHOTO_PICKER);
                check = 1;

            }
        });






            photoPicker = (Button)view.findViewById(R.id.UserImageUploadedButton);
        photoPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(check == 1)
                {
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Please Select An Image", Toast.LENGTH_SHORT).show();
                }


            }
        });

        skip = (TextView)view.findViewById(R.id.SKIP);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });







        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImageUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();


            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView)view.findViewById(R.id.UserImage);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

//            StorageReference photoReference =
//                    mChatPhotoStorageReference.child(selectedImageUri.getLastPathSegment());
//            photoReference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri downloadUri = taskSnapshot.getDownloadUrl();
//                    User userPhoto = new User(null, mUsername, downloadUri.toString());
//                    mMessageDatabaseRefrence.push().setValue(userPhoto);
//
//                }
//            });

        }
    }




}
