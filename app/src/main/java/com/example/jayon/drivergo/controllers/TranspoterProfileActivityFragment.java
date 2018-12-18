package com.example.jayon.drivergo.controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.adapters.TextTabsAdapter;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.fragments.TransporterAboutFragment;
import com.example.jayon.drivergo.fragments.TransporterOffersFragment;
import com.example.jayon.drivergo.models.FollowModel;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class TranspoterProfileActivityFragment extends Fragment {
    DatabaseReference reference;
    ViewPager viewPager;
    AlertDialog.Builder builder;
    List<Fragment> fragemtList=new ArrayList<>();
    List<String> titleList=new ArrayList<>();
    Button follow,contact;
    TextView name,email,sammary;
    CircleImageView imageProfile;
    ImageView location;
    KenBurnsView kenBurnsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_transpoter_profile, container, false);

        location=(ImageView)view.findViewById(R.id.location);
        follow=(Button)view.findViewById(R.id.follow);
        contact=(Button)view.findViewById(R.id.contact);
        imageProfile=(CircleImageView)view.findViewById(R.id.profileImage);
        name=(TextView)view.findViewById(R.id.name);
        email=(TextView)view.findViewById(R.id.email);
        sammary=(TextView)view.findViewById(R.id.sammary);
        viewPager=(ViewPager)view.findViewById(R.id.viewPager);
        kenBurnsView=(KenBurnsView)view.findViewById(R.id.KenBurnsView) ;

        TabLayout tabLayout=(TabLayout)view.findViewById(R.id.tab_layout);
        prepareData();

        TextTabsAdapter textTabsAdapter=new TextTabsAdapter(getFragmentManager(),fragemtList,titleList);

        viewPager.setAdapter(textTabsAdapter);

        tabLayout.setupWithViewPager(viewPager);

        final FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();



         reference= FirebaseDatabase.getInstance().getReference().child("users").child(Constant.transporter_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(imageProfile);
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(kenBurnsView);
                name.setText(dataSnapshot.child("firstName").getValue().toString()+" "+dataSnapshot.child("lastName").getValue().toString());
                email.setText(dataSnapshot.child("email").getValue().toString());
                sammary.setText(dataSnapshot.child("sammary").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
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

            }
        });


        DatabaseReference reference2= FirebaseDatabase.getInstance().getReference().child("follows");

        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    FollowModel model = data.getValue(FollowModel.class);
                    if (model.getMe().equals(currentUser.getUid()) && model.getTransporter().equals(Constant.transporter_id)){
                        follow.setText("Unfollow");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (follow.getText().equals("Follow")) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follows");
                    FollowModel followModel = new FollowModel(currentUser.getUid(), Constant.transporter_id);
                    reference.push().setValue(followModel);
                    follow.setText("Unfollow");

                }else if (follow.getText().equals("Unfollow")){
                    final DatabaseReference reference2= FirebaseDatabase.getInstance().getReference().child("follows");

                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot data: dataSnapshot.getChildren()) {
                                FollowModel model = data.getValue(FollowModel.class);
                                if (model.getMe().equals(currentUser.getUid()) && model.getTransporter().equals(Constant.transporter_id)){
                                    follow.setText("Follow");
                                    reference2.child(data.getKey()).removeValue();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(Constant.transporter_id);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + dataSnapshot.child("phone").getValue().toString()));

                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);

                            // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        } else {
                            //You already have permission
                            try {
                                startActivity(intent);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        return view;
    }

    public void prepareData(){
        addData(new TransporterAboutFragment(),"About ");
        addData(new TransporterOffersFragment(),"Offers");

    }

    public void  addData(Fragment fragment ,String title){

        fragemtList.add(fragment);
        titleList.add(title);

    }

}
