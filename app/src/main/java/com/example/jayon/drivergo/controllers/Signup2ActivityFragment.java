package com.example.jayon.drivergo.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class Signup2ActivityFragment extends Fragment {

    private ProgressDialog progressDialog;
    UserModel userModel;
    DatabaseReference reference;
    EditText type,capacity;
    Button signup;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_signup2, container, false);
        progressDialog=new ProgressDialog(getActivity());

        type=(EditText)root.findViewById(R.id.type);
        capacity=(EditText)root.findViewById(R.id.capacite);
        signup=(Button)root.findViewById(R.id.registbutton);
        mAuth = FirebaseAuth.getInstance();

        userModel=(UserModel) getActivity().getIntent().getExtras().getSerializable("userModel");
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(type.getText())) {
                    Toast.makeText(getActivity(), "Enter your vehicle type !", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(capacity.getText())) {
                    Toast.makeText(getActivity(), "Enter your vehicle capacity !", Toast.LENGTH_SHORT).show();
                }else {
                    userModel.setTypeOfVehicule(type.getText().toString());
                    userModel.setCapacity(capacity.getText().toString());

                    progressDialog.setTitle("Registration User");
                    progressDialog.setMessage("Please wait while we create your account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);

                    progressDialog.show();

                    FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();




                    mAuth.createUserWithEmailAndPassword(userModel.getEmail(),userModel.getPassword())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        progressDialog.dismiss();

                                        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
                                        reference= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());


                                        reference.setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){

                                                    progressDialog.dismiss();

                                                    Intent intent=new Intent(getActivity(),AllOffersActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.putExtra("userModel",userModel);
                                                    startActivity(intent);

                                                }
                                            }
                                        });

                                    }

                                    else {
                                        progressDialog.hide();
                                        Toast.makeText(getActivity(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });

                }
            }
        });




        return root;
    }

}
