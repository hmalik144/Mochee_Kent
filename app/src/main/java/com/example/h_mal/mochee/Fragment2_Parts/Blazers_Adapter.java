package com.example.h_mal.mochee.Fragment2_Parts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.h_mal.mochee.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by h_mal on 04/01/2018.
 */

public class Blazers_Adapter extends ArrayAdapter<Blazers> {

    public static final String LOG_TAG = Fragment_Two.class.getName();

    public Blazers_Adapter(Activity context, ArrayList<Blazers> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_blazers, parent, false);
        }
        Blazers currentBlazers = getItem(position);

        ImageView blazerImageView = (ImageView) listItemView.findViewById(R.id.imageButton);
        Picasso.with(getContext())
                .load(currentBlazers.getIconURL())
                .placeholder(R.drawable.m)
                .into(blazerImageView);

        return listItemView;
    }
}