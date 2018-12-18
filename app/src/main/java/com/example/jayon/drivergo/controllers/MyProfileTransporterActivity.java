package com.example.jayon.drivergo.controllers;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.adapters.TextTabsAdapter;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.fragments.AboutFragment;
import com.example.jayon.drivergo.fragments.FollowersFragment;
import com.example.jayon.drivergo.fragments.OffersFragment;
import com.example.jayon.drivergo.fragments.RequestsFragment;
import com.example.jayon.drivergo.fragments.TransportersDealsFragment;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileTransporterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    AlertDialog.Builder builder;
    List<Fragment> fragemtList=new ArrayList<>();
    List<String> titleList=new ArrayList<>();
    DatabaseReference reference0;
    TextView name,navName,navEmail,email,sammary;
    CircleImageView imageProfile,navImage;
    ImageView location;
    KenBurnsView kenBurnsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_transporter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);

        location=(ImageView)findViewById(R.id.location);
        imageProfile=(CircleImageView)findViewById(R.id.profileImage);
        navImage=(CircleImageView)hView.findViewById(R.id.navImage);
        name=(TextView)findViewById(R.id.name);
        email=(TextView)findViewById(R.id.email);
        navName=(TextView)hView.findViewById(R.id.navName);
        navEmail=(TextView)hView.findViewById(R.id.navEmail);
        sammary=(TextView)findViewById(R.id.sammary);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        kenBurnsView=(KenBurnsView)findViewById(R.id.KenBurnsView) ;

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tab_layout);

        prepareData();

        TextTabsAdapter textTabsAdapter=new TextTabsAdapter(getSupportFragmentManager(),fragemtList,titleList);

        viewPager.setAdapter(textTabsAdapter);

        tabLayout.setupWithViewPager(viewPager);

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

        GPSTracker gps = new GPSTracker(this);

        gps.getLocation();
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        Log.v("Location getAltitude", latitude+"");
        Log.v("Location getLongitude", longitude+"");
        Constant.current_latitute=latitude+"";
        Constant.current_longitute=longitude+"";



         reference0 = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());



        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference0.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Constant.current_latitute=(dataSnapshot.child("latitude").getValue().toString());
                        Constant.current_longitute=(dataSnapshot.child("longitute").getValue().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);

            }
        });

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(imageProfile);
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(kenBurnsView);
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(navImage);
                name.setText(dataSnapshot.child("firstName").getValue().toString()+" "+dataSnapshot.child("lastName").getValue().toString());
                navName.setText(dataSnapshot.child("firstName").getValue().toString()+" "+dataSnapshot.child("lastName").getValue().toString());
                email.setText(dataSnapshot.child("email").getValue().toString());
                navEmail.setText(dataSnapshot.child("email").getValue().toString());
                sammary.setText(dataSnapshot.child("sammary").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void prepareData(){
        addData(new AboutFragment(),"About me");
        addData(new OffersFragment(),"My Offers");
        addData(new RequestsFragment(),"Requests");
        addData(new TransportersDealsFragment(),"My deals");
        addData(new FollowersFragment(),"Followers");
    }

    public void  addData(Fragment fragment ,String title){

        fragemtList.add(fragment);
        titleList.add(title);

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
        getMenuInflater().inflate(R.menu.my_profile_transporter, menu);
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

        if (id == R.id.AllOffers) {

            startActivity(new Intent(this,AllOffersActivity.class));

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
