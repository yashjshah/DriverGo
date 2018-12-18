package com.example.jayon.drivergo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.controllers.AllOffersActivity;
import com.example.jayon.drivergo.controllers.MyProfileTransporterActivity;
import com.example.jayon.drivergo.models.OfferModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OfferTransporterProfileAdapter extends RecyclerView.Adapter<OfferTransporterProfileAdapter.myholder> {

    List<OfferModel> list;
    Context context;
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    LayoutInflater inflater;

    public OfferTransporterProfileAdapter(Context context, List<OfferModel> list ){
        this.context=context;
        this.list=list;
        inflater= LayoutInflater.from(context);

    }

    @Override
    public myholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.offer_cardview,parent,false);

        myholder myholder=new myholder(view);

        return myholder;
    }

    @Override
    public void onBindViewHolder(final myholder holder, final int position) {

        OfferModel model=list.get(position);

        holder.description.setText(model.getDescription());
        holder.date.setText(model.getDate());
        holder.price.setText(model.getPrice()+" $");
        holder.id.setText(model.getId());
        holder.offer_id.setText(model.getOfferId());
        holder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

       DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("users").child(model.getId());
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(holder.image);
                holder.name.setText(dataSnapshot.child("firstName").getValue().toString()+" "+dataSnapshot.child("lastName").getValue().toString());
                holder.email.setText(dataSnapshot.child("address").getValue().toString());

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });

       holder.subscribe.setVisibility(View.INVISIBLE);


    }

    public  void additem(OfferModel offerModel, int postion){

        list.add(offerModel);
        notifyDataSetChanged();
        notifyItemInserted(postion);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class myholder extends RecyclerView.ViewHolder{

        public View container;
        View view;
        CircleImageView image ;
        TextView name,email,id,description,date,price,offer_id;
        Button subscribe;

        public myholder(View itemView) {
            super(itemView);
            container = itemView;

            view=itemView;
            id=(TextView)itemView.findViewById(R.id.id);
            offer_id=(TextView)itemView.findViewById(R.id.offer_id);
            name=(TextView)itemView.findViewById(R.id.name);
            price=(TextView)itemView.findViewById(R.id.price);
            email=(TextView)itemView.findViewById(R.id.email);
            date=(TextView)itemView.findViewById(R.id.date);
            description=(TextView)itemView.findViewById(R.id.description1);
            image=(CircleImageView)itemView.findViewById(R.id.profileImage);
            subscribe=(Button) itemView.findViewById(R.id.subscribe);

        }
    }


}
