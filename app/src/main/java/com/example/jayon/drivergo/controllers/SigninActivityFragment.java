package com.example.jayon.drivergo.controllers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SigninActivityFragment extends Fragment   {

    private ProgressDialog progressDialog;
    Button login;
    EditText email, password;
    private FirebaseAuth mAuth;
    String provider;

    LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        mAuth = FirebaseAuth.getInstance();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);



        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        GPSTracker gps = new GPSTracker(getActivity());

        gps.getLocation();
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        Log.v("Location getAltitude", latitude + "");
        Log.v("Location getLongitude", longitude + "");
        Constant.current_latitute = latitude + "";
        Constant.current_longitute = longitude + "";


        FirebaseUser user = mAuth.getCurrentUser();

        progressDialog = new ProgressDialog(getActivity());

        login = (Button) view.findViewById(R.id.signinbutton);
        email = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);

        InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(email.getWindowToken(),0);

        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText())) {
                    Toast.makeText(getActivity(), "Enter your email!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText())) {
                    Toast.makeText(getActivity(), "Enter your password!", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.setTitle("Logging");
                    progressDialog.setMessage("Please wait while we check your credentials.");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.child("role").getValue().equals("Client")) {
                                                    Intent intent = new Intent(getActivity(), AllOffersActivity.class);
                                                    startActivity(intent);

                                                } else if (dataSnapshot.child("role").getValue().equals("Transporter")) {
                                                    Intent intent = new Intent(getActivity(), MyProfileTransporterActivity.class);
                                                    startActivity(intent);

                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    } else {
                                        progressDialog.hide();
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getActivity(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                }

            }

        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser==null){
            //startActivity(new Intent(getActivity(),StartActivity.class));

        }

    }


}
