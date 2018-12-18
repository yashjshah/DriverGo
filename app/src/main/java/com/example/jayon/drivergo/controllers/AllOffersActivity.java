package com.example.jayon.drivergo.controllers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.adapters.OfferAdapter;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.fragments.AddOfferFragment;
import com.example.jayon.drivergo.models.OfferModel;
import com.example.jayon.drivergo.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllOffersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,AddOfferFragment.DialogListener {
    DatabaseReference reference1;
    List<OfferModel> list;
    FirebaseUser firebaseUser;
    OfferAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference reference2;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    int already=0;

    TextView navName,navEmail;
    CircleImageView navImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);

        navImage=(CircleImageView)hView.findViewById(R.id.navImage);
        navName=(TextView)hView.findViewById(R.id.navName);
        navEmail=(TextView)hView.findViewById(R.id.navEmail);

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(navImage);
                navName.setText(dataSnapshot.child("firstName").getValue().toString()+" "+dataSnapshot.child("lastName").getValue().toString());
                navEmail.setText(dataSnapshot.child("email").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        reference1= FirebaseDatabase.getInstance().getReference().child("offers");

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        list=new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter=new OfferAdapter(this,list);
        recyclerView.setAdapter(adapter);


        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    OfferModel model = data.getValue(OfferModel.class);
                    model.setOfferId(data.getKey());
                    adapter.additem(model,0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.v("trans",firebaseUser.getUid());

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddOfferFragment addOfferFragment=new AddOfferFragment();
                addOfferFragment.show(getSupportFragmentManager(),"");

            }
        });

         reference2= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                 if (!dataSnapshot.child("role").getValue().equals("Transporter")){
                   fab.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public void addOffer(String description, String date, String price) {
        String date2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        OfferModel offerModel=new OfferModel(firebaseUser.getUid(),description,date2,price);
        reference1.push().setValue(offerModel);
        adapter.additem(offerModel,0);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_offers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Myprofile) {
            FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child("role").getValue().equals("Client")) {
                        startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));


                    } else if (dataSnapshot.child("role").getValue().equals("Transporter")) {
                        startActivity(new Intent(getApplicationContext(),MyProfileTransporterActivity.class));

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            startActivity(new Intent(this,MyProfileActivity.class));

        } else if (id == R.id.Alltransporters) {
            startActivity(new Intent(this,AllTransportersActivity.class));

        } else if (id == R.id.Editprofile) {
            startActivity(new Intent(this,EditProfileActivity.class));

        } else if (id == R.id.lock) {
            startActivity(new Intent(this,IntroActivity.class));
            FirebaseAuth.getInstance().signOut();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
