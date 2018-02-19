package com.example.h_mal.mochee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.h_mal.mochee.Fragment4_Parts.InspirationItems;
import com.example.h_mal.mochee.Fragment4_Parts.InspirationItems_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by h_mal on 07/10/2017.
 */

public class Fragment_Four extends Fragment {

    DatabaseReference inspDb;
    ListView listView;
    ArrayList<InspirationItems> inspirationItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment4, container, false);

        inspDb = FirebaseDatabase.getInstance().getReference("inspirationItems");

        inspirationItems = new ArrayList<InspirationItems>();

        InspirationItems_Adapter adapter = new InspirationItems_Adapter(getActivity(),inspirationItems);
        listView = (ListView) rootView.findViewById(R.id.insp_list);

        listView.setAdapter(adapter);
        
        inspDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //clearing the previous artist list
                inspirationItems.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    InspirationItems artist = postSnapshot.getValue(InspirationItems.class);
                    //adding artist to the list
                    inspirationItems.add(artist);
                }

                //creating adapter
                InspirationItems_Adapter adapter = new InspirationItems_Adapter(getActivity(),inspirationItems);
                //attaching adapter to the listview
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //attaching value event listener

    }

}
