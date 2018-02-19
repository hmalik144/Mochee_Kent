package com.example.h_mal.mochee.Fragment2_Parts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.h_mal.mochee.ImageViewer.ImageViewer;
import com.example.h_mal.mochee.Item_overview;
import com.example.h_mal.mochee.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by h_mal on 07/10/2017.
 */

public class Fragment_Two extends Fragment{

    private Blazers currentBlazer;
    ArrayList<Blazers> blazers;
    DatabaseReference stockDB;

    ImageView mainIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment2, container, false);

        FirebaseApp.initializeApp(getContext());

        mainIV = (ImageView) rootView.findViewById(R.id.imageView5);
        ImageView zoom = rootView.findViewById(R.id.imageView4);
        final TextView nameTV = (TextView) rootView.findViewById(R.id.blazer_nameTV);
        final TextView priceTV = (TextView) rootView.findViewById(R.id.blazer_priceTV);
        final GridView listView = (GridView) rootView.findViewById(R.id.list);

        blazers = new ArrayList<Blazers>();

        stockDB = FirebaseDatabase.getInstance().getReference("Stock");

        stockDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                blazers.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Blazers artist = postSnapshot.getValue(Blazers.class);
                    //adding artist to the list
                    blazers.add(artist);
                }
                currentBlazer = blazers.get(0);

                Picasso.with(getContext())
                        .load(currentBlazer.getImageURL())
                        .placeholder(R.drawable.mocheeloading)
                        .into(mainIV);

                nameTV.setText(currentBlazer.getName());
                priceTV.setText(currentBlazer.getPrice());
                //creating adapter
                Blazers_Adapter adapter = new Blazers_Adapter(getActivity(),blazers);
                //attaching adapter to the listview
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        zoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImageViewer.class);
                intent.putExtra("image",currentBlazer.getImageURL());
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentBlazer = (Blazers) parent.getItemAtPosition(position);
                Picasso.with(getContext())
                        .load(currentBlazer.getImageURL())
                        .placeholder(R.drawable.mocheeloading)
                        .into(mainIV);

                nameTV.setText(currentBlazer.getName());
                priceTV.setText(currentBlazer.getPrice());
            }
        });


        mainIV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenWindow();
            }
        });

        return rootView;
    }

    private void OpenWindow(){
        Intent intent = new Intent(Fragment_Two.this.getActivity(), Item_overview.class);

        intent.putExtra("image",currentBlazer.getImageURL());
        intent.putExtra("name",currentBlazer.getName());
        intent.putExtra("price",currentBlazer.getPrice());
        startActivity(intent);
    }

    private void addItemsFromFireBase(){


    }




}
