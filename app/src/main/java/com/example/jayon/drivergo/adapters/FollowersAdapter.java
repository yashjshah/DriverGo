package com.example.jayon.drivergo.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.constants.Constant;
import com.example.jayon.drivergo.controllers.AllTransportersActivity;
import com.example.jayon.drivergo.controllers.ClientProfileActivity;
import com.example.jayon.drivergo.controllers.TranspoterProfileActivity;
import com.example.jayon.drivergo.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.myholder> {


    List<UserModel> list;
    Context context;

    LayoutInflater inflater;
    public FollowersAdapter(Context context, List<UserModel> list){
        this.context=context;
        this.list=list;
        inflater= LayoutInflater.from(context);


    }

    @Override
    public myholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.transpoter_card_view,parent,false);

        myholder myholder=new myholder(view);

        return myholder;
    }

    @Override
    public void onBindViewHolder(final myholder holder, final int position) {

        final UserModel model=list.get(position);

        Picasso.get().load(model.getImage()).into(holder.profile_image);


        holder.name.setText(model.getFirstName()+" "+model.getLastName());
        holder.status.setText(model.getAddress());
        holder.id.setText(model.getId());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context, AllTransportersActivity.class);
                intent.putExtra("id",holder.id.getText());
                context.startActivity(intent);
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ClientProfileActivity.class));
                Constant.transporter_id=model.getId();
            }
        });

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ClientProfileActivity.class));
                Constant.transporter_id=model.getId();
            }
        });
    }

    public  void additem(UserModel userModel, int postion){

        list.add(userModel);
        notifyDataSetChanged();
        notifyItemInserted(postion);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class myholder extends RecyclerView.ViewHolder{


        View view;
        CircleImageView profile_image ;
        TextView name,status,id;

        public myholder(View itemView) {
            super(itemView);

            view=itemView;

            profile_image=(CircleImageView)itemView.findViewById(R.id.profile_image);
            name=(TextView) itemView.findViewById(R.id.name);
            status=(TextView) itemView.findViewById(R.id.sammary);
            id=(TextView) itemView.findViewById(R.id.id);

        }
    }


}
