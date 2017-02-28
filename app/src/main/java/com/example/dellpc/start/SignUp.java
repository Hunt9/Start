package com.example.dellpc.start;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.facebook.GraphRequest.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment {

    private View view;

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText contactNo;
    private EditText password;

    private Button signUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseRefrence;

    private String fName;
    private String lName;
    private String eMail;
    private String pAssword;
    private String cOntact;


    public SignUp() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        firstName = (EditText)view.findViewById(R.id.FirstName);
        lastName = (EditText)view.findViewById(R.id.LastName);
        email = (EditText)view.findViewById(R.id.Email);
        contactNo = (EditText)view.findViewById(R.id.ContactNumber);
        password = (EditText)view.findViewById(R.id.SignupPass);

        signUp = (Button)view.findViewById(R.id.SignUP);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fName = firstName.getText().toString();
                lName = lastName.getText().toString();
                eMail = email.getText().toString();
                pAssword = password.getText().toString();
                cOntact = contactNo.getText().toString();

                goSignUp();
            }
        });







    return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void goSignUp()
    {
        mAuth.createUserWithEmailAndPassword(eMail, pAssword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        User userz = new User(eMail,fName,lName,cOntact,null);
                        mMessageDatabaseRefrence.push().setValue(userz);

                        if (!task.isSuccessful()) {

                            Toast.makeText(getActivity(), "Failed to Sign up !!!", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

}
