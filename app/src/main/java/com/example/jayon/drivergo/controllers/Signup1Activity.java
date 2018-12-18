package com.example.jayon.drivergo.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.models.UserModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Signup1Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText firstName,lastName,email,passwordet,phone,date;
    Spinner roleSpinner;
    Button signup;
    private ProgressDialog progressDialog;
    DatabaseReference reference;
    UserModel userModel;
    String  location,latitute,longitute;
    PlaceAutocompleteFragment autocompleteFragment;
    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        progressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        signup=(Button)findViewById(R.id.registbutton);
        firstName=(EditText)findViewById(R.id.firstName);
        lastName=(EditText)findViewById(R.id.lastName);
        email=(EditText)findViewById(R.id.email);
        passwordet=(EditText)findViewById(R.id.passwordet);
        phone=(EditText)findViewById(R.id.phone);
        date=(EditText)findViewById(R.id.date);

        roleSpinner=(Spinner)findViewById(R.id.roleSpinner);

        ArrayList<String> list=new ArrayList<>();
        list.add("Client");
        list.add("Transporter");

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                autocompleteFragment.setText(place.getName()+"");
                location=place.getName()+"";
                Log.i("Place", "Place: " + location);
                latitute=place.getLatLng().latitude+"";
                longitute=place.getLatLng().longitude+"";
                Log.v("Location latitude", place.getLatLng().latitude + "");
                Log.v("Location longitude",  place.getLatLng().longitude + "");


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Place", "An error occurred: " + status);
            }
        });

        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(phone.getWindowToken(),0);

        getFragmentManager().executePendingTransactions();
        ((View)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);

        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(13.0f);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextColor(getApplicationContext().getResources().getColor(R.color.white));

        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setBackgroundResource(R.drawable.edittext);

        autocompleteFragment.setHint("Address");

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("USA")
                .build();

        autocompleteFragment.setFilter(typeFilter);




        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,list);
        roleSpinner.setAdapter(adapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(firstName.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter your first name!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(lastName.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter your first name!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(phone.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter your phone!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(date.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter your birth date!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(email.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(passwordet.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter your password!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(location)) {
                    Toast.makeText(getApplicationContext(), "Enter your address!", Toast.LENGTH_SHORT).show();
                }
                else if (passwordet.getText().length() < 7) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 7 characters!", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog.setTitle("Registration User");
                    progressDialog.setMessage("Please wait while we create your account");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);

                    progressDialog.show();



                    userModel=new UserModel("https://firebasestorage.googleapis.com/v0/b/drivergo-94873.appspot.com/o/profile.png?alt=media&token=f99699cc-e747-4de1-8c8d-d5e591a0aac9","Hello, there! iam using Driver Go.",firstName.getText().toString(),lastName.getText().toString(),email.getText().toString(),phone.getText().toString(),date.getText().toString(),location,latitute,longitute,roleSpinner.getSelectedItem().toString(),"","");
                    userModel.setPassword(passwordet.getText().toString());
                    if (roleSpinner.getSelectedItem().equals("Client")){

                        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), passwordet.getText().toString())
                                .addOnCompleteListener(Signup1Activity.this, new OnCompleteListener<AuthResult>() {
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

                                                        Intent intent=new Intent(getApplicationContext(),AllOffersActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        intent.putExtra("userModel",userModel);
                                                        startActivity(intent);

                                                    }
                                                }
                                            });

                                        }

                                        else {
                                            progressDialog.hide();
                                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        // ...
                                    }
                                });

                    }else if (roleSpinner.getSelectedItem().equals("Transporter")){

                        Intent intent=new Intent(getApplicationContext(),Signup2Activity.class);
                        intent.putExtra("userModel", userModel);
                        startActivity(intent);

                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("placexx", "Place: " + place.getLocale());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("place", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


}
