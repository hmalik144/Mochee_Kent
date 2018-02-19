package com.example.h_mal.mochee.Fragment4_Parts;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.h_mal.mochee.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by h_mal on 04/01/2018.
 */

public class InspirationItems_Adapter extends ArrayAdapter<InspirationItems> {

    public InspirationItems_Adapter(Activity context, ArrayList<InspirationItems> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_insp, parent, false);
        }
        InspirationItems currentItem = getItem(position);

        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.insp_desc);
        descriptionTextView.setText(currentItem.getDescription());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.insp_image);
        Picasso.with(getContext())
                .load(currentItem.getImageURL())
                .placeholder(R.drawable.mocheeloading)
                .into(imageView);

        TextView titleView = (TextView) listItemView.findViewById(R.id.insp_title);
        titleView.setText(currentItem.getName());

        TextView subtitletv = (TextView) listItemView.findViewById(R.id.insp_title2);
        subtitletv.setText(currentItem.getSubname());

        return listItemView;
    }

}
