package com.example.dellpc.start;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int pos = 0;
    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    public static final int RC_SIGN_IN = 1;
    public static final int RC_DEKHO = 2;
    public int DEKHAO = 0;
    public static final String FRIENDLY_MSG_LENGTH_KEY = "friendly_msg_key";

    private String mUsername;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseRefrence;

    private ChildEventListener mChildEventListner;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListner;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotoStorageReference;

    private FirebaseRemoteConfig firebaseRemoteConfig;

    private FirebaseMessagingService mFirebaseMessagingService;

    private String notifications;

    private ProgressDialog pDialog;

    private UserAdapter mUserAdapter;

    private ListView mUserListView;

    private String key;


    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    // File url to download
    private static String file_url;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private String UserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUsername = ANONYMOUS;




        mUserListView = (ListView)findViewById(R.id.UzerzzzList);

      //  mFirebaseDatabase.setPersistenceEnabled(true);

        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mFirebaseAuth = mFirebaseAuth.getInstance();
        mFirebaseStorage = mFirebaseStorage.getInstance();
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();



        mMessageDatabaseRefrence = mFirebaseDatabase.getReference().child("User");
        mChatPhotoStorageReference = mFirebaseStorage.getReference().child("user_photos");


        List<User> friendlyMessages = new ArrayList<>();
        mUserAdapter = new UserAdapter(this, R.layout.user_list, friendlyMessages);
        mUserListView.setAdapter(mUserAdapter);


        mAuthStateListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                UserID = user.getUid();
                if (user != null) {

                    if(DEKHAO == RC_DEKHO)
                    {
                        save(user.getUid(),user.getDisplayName(),user.getEmail());

                    }

                    OnSignedInInitialized(user.getDisplayName());

                } else {
                    OnSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);

                }

            }
        };

    }

    private void save(String uid, String displayName, String email) {


        User userz = new User(uid,displayName,email,null,null);
        mMessageDatabaseRefrence.push().setValue(userz);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                DEKHAO = RC_DEKHO;
                Toast.makeText(this, "Signed in", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.Add_Profile_Pic:
                Intent intent = new Intent(MainActivity.this, Photos.class);
                intent.putExtra("key",key);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuthStateListner != null) {
            mFirebaseAuth.addAuthStateListener(mAuthStateListner);
        }
        dettachDatabaseReadListner();
        mUserAdapter.clear();

    }

    private void OnSignedInInitialized(String displayName) {
        mUsername = displayName;
        attachDatabaseReadListner();
    }


    private void OnSignedOutCleanUp() {
        mUsername = ANONYMOUS;
        mUserAdapter.clear();
    }

    private void attachDatabaseReadListner() {
        if (mChildEventListner == null) {
            mChildEventListner = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    User alltheUserz = dataSnapshot.getValue(User.class);
                    alltheUserz.setKey(dataSnapshot.getKey());
                    key = alltheUserz.getKey();
                    mUserAdapter.add(alltheUserz);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };

            mMessageDatabaseRefrence.addChildEventListener(mChildEventListner);
        }

    }

    private void dettachDatabaseReadListner() {
        if (mChildEventListner != null) {
            mMessageDatabaseRefrence.removeEventListener(mChildEventListner);
            mChildEventListner = null;
        }
    }



}
