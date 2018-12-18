package com.example.jayon.drivergo.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jayon.drivergo.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    DatabaseReference reference;
    CircleImageView profileImage;
    FirebaseUser firebaseUser;
    EditText firstName,lastName,sammary,phone,date,type,capacite;
    Button edit;
    StorageReference storageReference;
    Uri uri;
    PlaceAutocompleteFragment autocompleteFragment;
    String location,latitute,longitute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        progressDialog=new ProgressDialog(this);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        profileImage=(CircleImageView)findViewById(R.id.profileImage);
        firstName=(EditText)findViewById(R.id.firstName);
        lastName=(EditText)findViewById(R.id.lastName);
        sammary=(EditText)findViewById(R.id.sammary);
        phone=(EditText)findViewById(R.id.phone);
        date=(EditText)findViewById(R.id.date);
        capacite=(EditText)findViewById(R.id.capacite);
        type=(EditText)findViewById(R.id.type);

        edit=(Button)findViewById(R.id.edit);
        storageReference= FirebaseStorage.getInstance().getReference();


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




        reference= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("role").getValue().equals("Client")){
                    type.setVisibility(View.GONE);
                    capacite.setVisibility(View.GONE);
                }
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(profileImage);

                firstName.setText(dataSnapshot.child("firstName").getValue().toString());
                lastName.setText(dataSnapshot.child("lastName").getValue().toString());
                phone.setText(dataSnapshot.child("phone").getValue().toString());
                location=dataSnapshot.child("address").getValue().toString();
                latitute=dataSnapshot.child("latitude").getValue().toString();
                longitute=dataSnapshot.child("longitute").getValue().toString();
                autocompleteFragment.setText(dataSnapshot.child("address").getValue().toString());
                capacite.setText(dataSnapshot.child("capacity").getValue().toString());
                type.setText(dataSnapshot.child("typeOfVehicule").getValue().toString());
                date.setText(dataSnapshot.child("date").getValue().toString());
                sammary.setText(dataSnapshot.child("sammary").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select image"),1);
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
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

                else {

                    progressDialog.setTitle("Updating your information");
                    progressDialog.setMessage("Please wait while we updating your information");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);
                    progressDialog.show();

                    reference.child("firstName").setValue(firstName.getText().toString());
                    reference.child("lastName").setValue(lastName.getText().toString());
                    reference.child("phone").setValue(phone.getText().toString());
                    reference.child("address").setValue(location);
                    reference.child("longitute").setValue(longitute);
                    reference.child("latitude").setValue(latitute);
                    reference.child("capacity").setValue(capacite.getText().toString());
                    reference.child("typeOfVehicule").setValue(type.getText().toString());
                    reference.child("date").setValue(date.getText().toString());
                    reference.child("sammary").setValue(sammary.getText().toString());

                    progressDialog.dismiss();

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("role").getValue().equals("Client")){
                                startActivity(new Intent(getApplicationContext(),AllOffersActivity.class));
                            }
                            else if (dataSnapshot.child("role").getValue().equals("Transporter")){
                                startActivity(new Intent(getApplicationContext(),MyProfileTransporterActivity.class));
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && requestCode == 1) {

            progressDialog.setTitle("Uploading photo");
            progressDialog.setMessage("Please wait while we change your photo");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.BUTTON_NEUTRAL);

            progressDialog.show();
            uri = data.getData();
            Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();

            final StorageReference filePath = storageReference.child("user_images").child(firebaseUser.getUid() + ".jpg");
            UploadTask uploadTask = filePath.putFile(uri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        reference.child("image").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
                                    Toast.makeText(getApplicationContext(), "Success Uploading!", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                        Picasso.get().load(downloadUri.toString()).into(profileImage);


                    } else {
                        progressDialog.hide();

                        // Handle failures
                        // ...
                    }
                }
            });


        }

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10 );
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
