package com.example.jayon.drivergo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.adapters.AboutProfileAdapter;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.models.AboutRecyclerViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TransporterAboutFragment extends Fragment {

    AboutProfileAdapter adapter;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    List<AboutRecyclerViewModel> list;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.aboutfragment,container,false);


        recyclerView=(RecyclerView)root.findViewById(R.id.profileRecyclerView);
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

         adapter=new AboutProfileAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(Constant.transporter_id);
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



        return root;
    }
}
