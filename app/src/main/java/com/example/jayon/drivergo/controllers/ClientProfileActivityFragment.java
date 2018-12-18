package com.example.jayon.drivergo.controllers;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.adapters.AboutProfileAdapter;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.models.AboutRecyclerViewModel;
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
public class ClientProfileActivityFragment extends Fragment {
    AlertDialog.Builder builder;
    List<Fragment> fragemtList=new ArrayList<>();
    List<String> titleList=new ArrayList<>();
    TextView name,email,sammary;
    CircleImageView imageProfile;
    ImageView location;
    DatabaseReference reference;
    AboutProfileAdapter adapter;
    KenBurnsView kenBurnsView;

    List<AboutRecyclerViewModel> list;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_client_profile, container, false);

        location=(ImageView)view.findViewById(R.id.location);
        imageProfile=(CircleImageView)view.findViewById(R.id.profileImage);
        name=(TextView)view.findViewById(R.id.name);
        email=(TextView)view.findViewById(R.id.email);
        sammary=(TextView)view.findViewById(R.id.sammary);
        kenBurnsView=(KenBurnsView)view.findViewById(R.id.KenBurnsView) ;

        Log.i("client",Constant.transporter_id );
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


        recyclerView=(RecyclerView)view.findViewById(R.id.profileRecyclerView);
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

        adapter=new AboutProfileAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
         reference= FirebaseDatabase.getInstance().getReference().child("users").child(Constant.transporter_id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                AboutRecyclerViewModel typeOfVehicule=new AboutRecyclerViewModel(R.drawable.carpickup,"Type of vehicle ",dataSnapshot.child("typeOfVehicule").getValue().toString());
                AboutRecyclerViewModel capacity=new AboutRecyclerViewModel(R.drawable.weightkilogram,"Capacity of vehicle",dataSnapshot.child("capacity").getValue().toString());
                AboutRecyclerViewModel zone=new AboutRecyclerViewModel(R.drawable.ic_location_on_black_24dp,"Address",dataSnapshot.child("address").getValue().toString());
                AboutRecyclerViewModel date=new AboutRecyclerViewModel(R.drawable.calendar_range,"Birth Date",dataSnapshot.child("date").getValue().toString());
                AboutRecyclerViewModel phone=new AboutRecyclerViewModel(R.drawable.calendar_range,"phone",dataSnapshot.child("phone").getValue().toString());

                if (dataSnapshot.child("role").getValue().equals("Transporter")){
                    adapter.additem(typeOfVehicule,0);
                    adapter.additem(capacity,0);
                }

                adapter.additem(zone,0);
                adapter.additem(date,0);
                adapter.additem(phone,0);

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

        return view;
    }
}
