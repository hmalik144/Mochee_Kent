package com.example.h_mal.mochee.Fragment3_Parts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.h_mal.mochee.R;

import java.util.ArrayList;

/**
 * Created by h_mal on 04/01/2018.
 */

public class Bespoke_Adapter extends ArrayAdapter<Bespoke> {

    public Bespoke_Adapter(Activity context, ArrayList<Bespoke> bespoke) {
        super(context, 0, bespoke);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_bespoke, parent, false);
        }
        Bespoke currentItem = getItem(position);

        ImageView itemImageView = (ImageView) listItemView.findViewById(R.id.bespoke_logo);
        itemImageView.setImageResource(currentItem.getLogo());

        RelativeLayout relativeLayout = listItemView.findViewById(R.id.bespoke_layout);
        relativeLayout.setBackgroundResource(currentItem.getBackgroundColour());

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.bespoke_title);
        titleTextView.setText(currentItem.getTitle());
        titleTextView.setTextColor(currentItem.getTitleTextColour());

        TextView descTextView = (TextView) listItemView.findViewById(R.id.bespoke_description);
        descTextView.setText(currentItem.getDescription());
        descTextView.setTextColor(currentItem.getDescriptionTextColour());

        return listItemView;
    }
}