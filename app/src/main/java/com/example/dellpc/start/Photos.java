package com.example.dellpc.start;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Photos extends AppCompatActivity {

    private static final int RC_PHOTO_PICKER = 2 ;
    private Button photoPicker;
    private ImageView myImage;
    private TextView skip;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseRefrence;

    private ChildEventListener mChildEventListner;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotoStorageReference;

    private int check = 0;

    private String keyz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mFirebaseAuth = mFirebaseAuth.getInstance();
        mFirebaseStorage = mFirebaseStorage.getInstance();


        mMessageDatabaseRefrence = mFirebaseDatabase.getReference().child("User");
        mChatPhotoStorageReference = mFirebaseStorage.getReference().child("user_photos");

        keyz

        myImage = (ImageView)findViewById(R.id.UserImage);
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






        photoPicker = (Button)findViewById(R.id.UserImageUploadedButton);
        photoPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(check == 1)
                {
                    Toast.makeText(Photos.this, "Uploaded", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Photos.this, "Please Select An Image", Toast.LENGTH_SHORT).show();
                }


            }
        });

        skip = (TextView)findViewById(R.id.SKIP);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });




    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImageUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();


            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView)findViewById(R.id.UserImage);
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
