package com.example.jayon.drivergo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.jayon.drivergo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by MOHAMED on 10/26/2018.
 */

public class AddOfferFragment extends AppCompatDialogFragment {

    EditText description,date,price;
    DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.add_offer_layout,null);
        dialog.setView(view);
        dialog.setPositiveButton("I agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (TextUtils.isEmpty(description.getText())) {
                    Toast.makeText(getActivity(), "Enter your Offer description!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(price.getText())) {
                    Toast.makeText(getActivity(), "Enter your offer price!", Toast.LENGTH_SHORT).show();
                }
                else if (isNumeric(price.getText().toString())==false) {
                    Toast.makeText(getActivity(), "Price must be number!", Toast.LENGTH_SHORT).show();
                }else {

                    listener.addOffer(description.getText().toString(), date.getText().toString(), price.getText().toString());
                }
            }
        });

        dialog.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        description=view.findViewById(R.id.Description);
        date=view.findViewById(R.id.date);
        price=view.findViewById(R.id.price);



        return dialog.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener=(DialogListener)context;
    }

    public interface DialogListener{
        public void addOffer(String description,String date,String price);
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
