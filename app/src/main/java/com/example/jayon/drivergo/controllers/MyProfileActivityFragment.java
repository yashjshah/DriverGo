package com.example.jayon.drivergo.controllers;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.adapters.TextTabsAdapter;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.fragments.AboutFragment;
import com.example.jayon.drivergo.fragments.FollowFragment;
import com.example.jayon.drivergo.fragments.OffersFragment;
import com.example.jayon.drivergo.fragments.SubscriptionsFragment;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MyProfileActivityFragment extends Fragment {

    ViewPager viewPager;
    AlertDialog.Builder builder;
    List<Fragment> fragemtList=new ArrayList<>();
    List<String> titleList=new ArrayList<>();

    TextView name,email,sammary;
    CircleImageView imageProfile;
    ImageView location;
    KenBurnsView kenBurnsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_my_profile, container, false);
        imageProfile=(CircleImageView)view.findViewById(R.id.profileImage);
        name=(TextView)view.findViewById(R.id.name);
        email=(TextView)view.findViewById(R.id.email);
        sammary=(TextView)view.findViewById(R.id.sammary);
        viewPager=(ViewPager)view.findViewById(R.id.viewPager);
        location=(ImageView)view.findViewById(R.id.location);
        kenBurnsView=(KenBurnsView)view.findViewById(R.id.KenBurnsView) ;

        TabLayout tabLayout=(TabLayout)view.findViewById(R.id.tab_layout);

        prepareData();

        TextTabsAdapter textTabsAdapter=new TextTabsAdapter(getFragmentManager(),fragemtList,titleList);

        viewPager.setAdapter(textTabsAdapter);

        tabLayout.setupWithViewPager(viewPager);

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();


        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());



        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Constant.current_latitute=(dataSnapshot.child("latitude").getValue().toString());
                        Constant.current_longitute=(dataSnapshot.child("longitute").getValue().toString());

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(imageProfile);
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(kenBurnsView);
                name.setText(dataSnapshot.child("firstName").getValue().toString()+" "+dataSnapshot.child("lastName").getValue().toString());
                email.setText(dataSnapshot.child("email").getValue().toString());
                sammary.setText(dataSnapshot.child("sammary").getValue().toString());
                if (dataSnapshot.child("role").getValue().toString().equals("Client") | dataSnapshot.child("email").getValue().toString().equals(email.getText()) ){
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }


    public void prepareData(){
        addData(new AboutFragment(),"About");
        addData(new SubscriptionsFragment(),"Subscriptions");
        addData(new FollowFragment(),"Follows");
    }

    public void  addData(Fragment fragment ,String title){

        fragemtList.add(fragment);
        titleList.add(title);

    }
}
