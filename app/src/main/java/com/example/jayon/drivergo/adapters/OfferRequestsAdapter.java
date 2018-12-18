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
import com.example.jayon.drivergo.controllers.ClientProfileActivity;
import com.example.jayon.drivergo.models.SubscribeModel;
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


public class OfferRequestsAdapter extends RecyclerView.Adapter<OfferRequestsAdapter.myholder> {

    List<SubscribeModel> list;
    Context context;
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    LayoutInflater inflater;

    public OfferRequestsAdapter(Context context, List<SubscribeModel> list ){
        this.context=context;
        this.list=list;
        inflater= LayoutInflater.from(context);

    }

    @Override
    public myholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.offer_request_cardview,parent,false);

        myholder myholder=new myholder(view);

        return myholder;
    }

    @Override
    public void onBindViewHolder(final myholder holder, final int position) {

        final SubscribeModel model=list.get(position);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("offers").child(model.getOfferID());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 holder.description.setText(dataSnapshot.child("description").getValue().toString());
                 holder.date.setText(dataSnapshot.child("date").getValue().toString());
                holder.price.setText(dataSnapshot.child("price").getValue().toString()+" $");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.id.setText(model.getId());
        holder.offer_id.setText(model.getOfferID());
        holder.from.setText(model.getFrom());
        holder.to.setText(model.getTo());


        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        holder.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("subscriptions").child(model.getId());
                reference.child("status").setValue("true");
                holder.agree.setText("Subscribed");
                holder.cancel.setVisibility(View.INVISIBLE);

            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,list.size());
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("subscriptions").child(model.getId());
                reference.removeValue();
                holder.cancel.setText("Canceled");
                holder.agree.setVisibility(View.GONE);


            }
        });

       DatabaseReference reference0= FirebaseDatabase.getInstance().getReference().child("users").child(model.getMe());
       reference0.addValueEventListener(new ValueEventListener() {
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


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ClientProfileActivity.class));
                Constant.transporter_id=model.getMe();
            }
        });


    }


    public  void additem(SubscribeModel subscribeModel, int postion){

        list.add(subscribeModel);
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
        TextView name,email,id,description,date,price,offer_id,from,to;
        Button cancel,agree;

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
            cancel=(Button) itemView.findViewById(R.id.cancel);
            agree=(Button) itemView.findViewById(R.id.agree);
            from=(TextView)itemView.findViewById(R.id.from);
            to=(TextView)itemView.findViewById(R.id.to);

        }
    }


}
