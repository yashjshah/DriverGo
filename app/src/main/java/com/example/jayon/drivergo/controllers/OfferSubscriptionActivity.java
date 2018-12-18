package com.example.jayon.drivergo.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.models.OfferModel;
import com.example.jayon.drivergo.models.SubscribeModel;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class OfferSubscriptionActivity extends AppCompatActivity {
    CircleImageView image ;
    TextView name,email,id,description,date,price,offer_id;
    Button subscribe;
    String locationA,locationB;
     PlaceAutocompleteFragment autocompleteFragment,autocompleteFragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_subscription);

        id=(TextView)findViewById(R.id.id);
        offer_id=(TextView)findViewById(R.id.offer_id);
        name=(TextView)findViewById(R.id.name);
        price=(TextView)findViewById(R.id.price);
        email=(TextView)findViewById(R.id.email);
        date=(TextView)findViewById(R.id.date);
        description=(TextView)findViewById(R.id.description1);
        image=(CircleImageView)findViewById(R.id.profileImage);
        subscribe=(Button) findViewById(R.id.subscribe);

        OfferModel model = (OfferModel) getIntent().getSerializableExtra("offer");

        description.setText(model.getDescription());
        date.setText(model.getDate());
        price.setText(model.getPrice()+" $");
        id.setText(model.getId());
        offer_id.setText(model.getOfferId());

       image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(getApplicationContext(), TranspoterProfileActivity.class));
               Constant.transporter_id=id.getText().toString();
           }
       });

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(model.getId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(image);
                name.setText(dataSnapshot.child("firstName").getValue().toString()+" "+dataSnapshot.child("lastName").getValue().toString());
                email.setText(dataSnapshot.child("address").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


          autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment2 = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment2);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                autocompleteFragment.setText(place.getName()+"");
                locationA=place.getName()+"";
                Log.i("Place", "Place: " + locationA);

                Log.v("Location latitude", place.getLatLng().latitude + "");
                Log.v("Location longitude",  place.getLatLng().longitude + "");


            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Place", "An error occurred: " + status);
            }
        });



        autocompleteFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //TODO: Get info about the selected place.
                autocompleteFragment2.setText(place.getName()+"");
                locationB=place.getName()+"";
                Log.i("Place", "Place: " + locationB);

            }

            @Override
            public void onError(Status status) {
                //TODO: Handle the error.
                Log.i("Place", "An error occurred: " + status);
            }
        });


        getFragmentManager().executePendingTransactions();
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(12.0f);
        ((EditText)autocompleteFragment2.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(12.0f);
        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        ((EditText)autocompleteFragment2.getView().findViewById(R.id.place_autocomplete_search_input)).setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setBackgroundResource(R.drawable.btn4);
        ((EditText)autocompleteFragment2.getView().findViewById(R.id.place_autocomplete_search_input)).setBackgroundResource(R.drawable.btn4);

        autocompleteFragment.setHint("From");
        autocompleteFragment2.setHint("To");

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("USA")
                .build();

        autocompleteFragment.setFilter(typeFilter);


        autocompleteFragment2.setFilter(typeFilter);


        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (locationA.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Enter location A!", Toast.LENGTH_SHORT).show();
                } else if (locationB.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Enter location B!", Toast.LENGTH_SHORT).show();
                } else {

                    if (subscribe.getText().equals("Subscribe")) {

                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("role").getValue().equals("Client")) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("subscriptions");
                                    SubscribeModel subscribeModel = new SubscribeModel(offer_id.getText().toString(), firebaseUser.getUid(), id.getText().toString(), "false",locationA,locationB);
                                    reference.push().setValue(subscribeModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                new AlertDialog.Builder(OfferSubscriptionActivity.this)
                                                        .setIcon(R.drawable.dribb)
                                                        .setTitle("Good job")
                                                        .setMessage("Your request sent to "+name.getText())
                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                startActivity(new Intent(getApplicationContext(),AllOffersActivity.class));
                                                            }
                                                        }).show();
                                            }
                                        }
                                    });
                                    subscribe.setText("Request sent");
                                } else if (dataSnapshot.child("role").getValue().equals("Transporter")) {
                                    AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_Material_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(getApplicationContext());
                                    }
                                    builder.setTitle("Warning")
                                            .setMessage("You are not be able to subscribe , because you are transporter ")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                }
                                            })

                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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
