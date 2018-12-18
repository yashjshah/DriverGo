package com.example.jayon.drivergo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.controllers.OfferSubscriptionActivity;
import com.example.jayon.drivergo.controllers.TranspoterProfileActivity;
import com.example.jayon.drivergo.fragments.AddLocationsFragment;
import com.example.jayon.drivergo.models.SubscribeModel;
import com.example.jayon.drivergo.models.OfferModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.myholder> {

    List<OfferModel> list;
    Context context;
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    LayoutInflater inflater;

    public OfferAdapter(Context context, List<OfferModel> list ){
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

        final OfferModel model=list.get(position);

        holder.description.setText(model.getDescription());
        holder.date.setText(model.getDate());
        holder.price.setText(model.getPrice()+" $");
        holder.id.setText(model.getId());
        holder.offer_id.setText(model.getOfferId());
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        holder.subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (holder.subscribe.getText().equals("Subscribe")){

                    DatabaseReference reference2= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
                    reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (!dataSnapshot.child("role").getValue().equals("Transporter")){
                                Intent intent=new Intent(context, OfferSubscriptionActivity.class);
                                intent.putExtra("offer", (Serializable) model);
                                context.startActivity(intent);
                            }else   if (dataSnapshot.child("role").getValue().equals("Transporter")) {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(context);
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



                }else       if (holder.subscribe.getText().equals("Request sent")) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Warning")
                            .setMessage("Your request already sent ")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
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

        DatabaseReference reference3= FirebaseDatabase.getInstance().getReference().child("subscriptions");
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    SubscribeModel model = data.getValue(SubscribeModel.class);
                    if (model.getMe().equals(firebaseUser.getUid()) && model.getTransporter().equals(holder.id.getText()) &&model.getOfferID().equals(holder.offer_id.getText())&& model.getStatus().equals("false")){
                        holder.subscribe.setText("Request sent");

                    }
                    else if (model.getMe().equals(firebaseUser.getUid()) && model.getTransporter().equals(holder.id.getText()) &&model.getOfferID().equals(holder.offer_id.getText())&& model.getStatus().equals("true")){
                        holder.subscribe.setText("Subscribed");

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       DatabaseReference reference2= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.child("role").getValue().equals("Transporter")){
                    holder.subscribe.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TranspoterProfileActivity.class));
                Constant.transporter_id=holder.id.getText().toString();
            }
        });

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
