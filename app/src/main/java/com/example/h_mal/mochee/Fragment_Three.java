package com.example.h_mal.mochee;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.h_mal.mochee.Fragment3_Parts.Bespoke;
import com.example.h_mal.mochee.Fragment3_Parts.Bespoke_Adapter;

import java.util.ArrayList;

/**
 * Created by h_mal on 07/10/2017.
 */

public class Fragment_Three extends Fragment{

    ImageView imMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment3, container, false);

        final ArrayList<Bespoke> bespoke = new ArrayList<Bespoke>();
        bespoke.add(new Bespoke(R.drawable.cloth,R.color.bespoke_bg1,"CHOOSE YOUR\nFABRIC", Color.WHITE,"Choose from our material library. A mixture of velvet, jamawar and jacquard patterns.", Color.BLACK));
        bespoke.add(new Bespoke(R.drawable.pen,R.color.bespoke_bg2,"DESIGN", R.color.bespoke_bg1,"Let our design team take charge to mock up a concept which you can personalise from the lapels to the cuff buttons. ", Color.WHITE));
        bespoke.add(new Bespoke(R.drawable.tailor,R.color.bespoke_bg3,"TAILOR\nMADE", R.color.bespoke_bg1,"Let our skilled tailors take charge in creating your master piece. Allow two week for a finished arcticle.", Color.BLACK));
        bespoke.add(new Bespoke(R.drawable.world,R.color.bespoke_bg4,"UK & WORLDWIDE DELIVERY", Color.BLACK,"We deliver to all UK destinations free of charge. To all other worldwide destinations we charge a small fee.", Color.BLACK));

        Bespoke_Adapter adapter = new Bespoke_Adapter(getActivity(),bespoke);
        ListView listView = (ListView) rootView.findViewById(R.id.list_bespoke);

        listView.setAdapter(adapter);

        return rootView;
    }

}
