package com.example.jayon.drivergo.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jayon.drivergo.R;
import com.example.jayon.drivergo.models.AboutRecyclerViewModel;

import java.util.List;


public class AboutProfileAdapter extends RecyclerView.Adapter<AboutProfileAdapter.myholder> {


   List<AboutRecyclerViewModel> list;
   Context context;

   LayoutInflater inflater;
   public AboutProfileAdapter(Context context, List<AboutRecyclerViewModel> list){
       this.context=context;
       this.list=list;
       inflater= LayoutInflater.from(context);


   }

   @Override
   public myholder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view=inflater.inflate(R.layout.about_profile_card_view,parent,false);

       myholder myholder=new myholder(view);

       return myholder;
   }

   @Override
   public void onBindViewHolder(final myholder holder, final int position) {

       AboutRecyclerViewModel model=list.get(position);

       holder.headerTextView.setText(model.getHeader());
       holder.valueTextView.setText(model.getValue());

       holder.iconImageView.setImageResource(model.getIcon());

       holder.editImageView.setVisibility(View.INVISIBLE);
   }


    public  void additem(AboutRecyclerViewModel aboutRecyclerViewModel, int postion){

        list.add(aboutRecyclerViewModel);
        notifyDataSetChanged();
        notifyItemInserted(postion);


    }

   @Override
   public int getItemCount() {
       return list.size();
   }


   class myholder extends RecyclerView.ViewHolder{


       ImageView iconImageView ,editImageView;
       TextView headerTextView,valueTextView;

       public myholder(View itemView) {
           super(itemView);

           iconImageView=(ImageView)itemView.findViewById(R.id.imageIcon);
           editImageView=(ImageView)itemView.findViewById(R.id.editImage);

           headerTextView=(TextView)itemView.findViewById(R.id.headerTextView);
           valueTextView=(TextView)itemView.findViewById(R.id.valuetextView);

       }
   }


}
