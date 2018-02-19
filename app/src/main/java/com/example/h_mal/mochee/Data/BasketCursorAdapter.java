package com.example.h_mal.mochee.Data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.h_mal.mochee.BasketActivity;
import com.example.h_mal.mochee.Basket_Counter;
import com.example.h_mal.mochee.Data.BasketContract.ItemEntry;
import com.example.h_mal.mochee.Item_overview;
import com.example.h_mal.mochee.MainActivity;
import com.example.h_mal.mochee.R;
import com.squareup.picasso.Picasso;

/**
 * Created by h_mal on 13/01/2018.
 */

public class BasketCursorAdapter extends CursorAdapter {

    private final BasketActivity activity;

    private Context mContext;

    public BasketCursorAdapter(BasketActivity context, Cursor c) {
        super(context, c, 0);
        this.activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_basket, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mContext = context;

        ImageView imageView = (ImageView) view.findViewById(R.id.basketIV);
        TextView nameTV = (TextView) view.findViewById(R.id.basketName);
        TextView priceTV = (TextView) view.findViewById(R.id.basketPrice);
        TextView quantityTV = (TextView) view.findViewById(R.id.basketQuantity);
        TextView sizeTV = (TextView) view.findViewById(R.id.basketSize);
        TextView totalPriceTV = (TextView) view.findViewById(R.id.basketTotalPrice);
        ImageView delete = (ImageView) view.findViewById(R.id.delete_item);

        final String imageColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.COLUMN_ITEM_IMAGE));
        final String nameColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.COLUMN_ITEM_NAME));
        final Float priceColumnIndex = cursor.getFloat(cursor.getColumnIndexOrThrow(ItemEntry.COLUMN_ITEM_PRICE));
        final String quantityInColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.COLUMN_ITEM_QUANTITY));
        final String sizeOutColumnIndex = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry.COLUMN_ITEM_SIZE));
        final Float totalpriceColumnIndex = cursor.getFloat(cursor.getColumnIndexOrThrow(ItemEntry.COLUMN_ITEM_TOTALPRICE));

        Picasso.with(mContext)
                .load(imageColumnIndex)
                .placeholder(R.drawable.mocheeloading)
                .into(imageView);

        nameTV.setText(nameColumnIndex);
        priceTV.setText("£"+String.format("%.2f", priceColumnIndex));
        quantityTV.setText("Quantity : " + quantityInColumnIndex);
        sizeTV.setText(sizeOutColumnIndex);
        totalPriceTV.setText("£"+String.format("%.2f", totalpriceColumnIndex));

        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(ItemEntry._ID));

        view.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                final Intent newIntent = new Intent(mContext, Item_overview.class);
                newIntent.putExtra("name",nameColumnIndex);
                newIntent.putExtra("price",priceColumnIndex);
                newIntent.putExtra("image",imageColumnIndex);
                mContext.startActivity(newIntent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mContext.getContentResolver().delete(ItemEntry.CONTENT_URI, ItemEntry._ID + " = " + id , null);
                if(Basket_Counter.setBasketQuantity(mContext) == 0){
                    final Intent intent = new Intent(mContext, MainActivity.class);
                    new AlertDialog.Builder(mContext)
                            .setTitle("Cart Is Empty")
                            .setCancelable(false)
                            .setNegativeButton("Return to Items", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    mContext.startActivity(intent);
                                }
                            })
                    .create().show();
                }
            }
        });

    }

}