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
import com.example.jayon.drivergo.adapters.AllTransportersAdapter;
import com.example.jayon.drivergo.adapters.FollowersAdapter;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.models.FollowModel;
import com.example.jayon.drivergo.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FollowersFragment extends Fragment {

    List<UserModel> list;
    FirebaseUser firebaseUser;
    FollowersAdapter adapter;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_followers, container, false);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);
        list=new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter=new FollowersAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

         DatabaseReference reference2= FirebaseDatabase.getInstance().getReference().child("follows");

            reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot data: dataSnapshot.getChildren()) {
                    final FollowModel model1 = data.getValue(FollowModel.class);
                    if ( model1.getTransporter().equals(firebaseUser.getUid())){
                        DatabaseReference  reference= FirebaseDatabase.getInstance().getReference().child("users").child(model1.getMe());

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                UserModel userModel=new UserModel();
                                userModel.setId(model1.getMe());
                                userModel.setFirstName(dataSnapshot.child("firstName").getValue().toString());
                                userModel.setLastName(dataSnapshot.child("lastName").getValue().toString());
                                userModel.setAddress(dataSnapshot.child("address").getValue().toString());
                                userModel.setImage(dataSnapshot.child("image").getValue().toString());
                                adapter.additem(userModel,0);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;

    }
}
