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
import com.example.jayon.drivergo.adapters.OfferTransporterProfileAdapter;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.models.OfferModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TransporterOffersFragment extends Fragment {

    OfferTransporterProfileAdapter offerRecyclerViewAdapter;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RecyclerView recyclerView;
    List<OfferModel> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.offersfragment,container,false);
        recyclerView=(RecyclerView)root.findViewById(R.id.offersRecyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        list=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("offers");

        final FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();


        offerRecyclerViewAdapter=new OfferTransporterProfileAdapter(getActivity(),list);
        recyclerView.setAdapter(offerRecyclerViewAdapter);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    OfferModel model = data.getValue(OfferModel.class);
                    model.setOfferId(data.getKey());
                    if (model.getId().equals(Constant.transporter_id)){
                    offerRecyclerViewAdapter.additem(model,0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
       //offerRecyclerViewAdapter.clear();

    }
}
