package com.example.h_mal.mochee;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h_mal.mochee.Data.BasketContract.ItemEntry;
import com.example.h_mal.mochee.Data.BasketDbHelper;
import com.example.h_mal.mochee.ImageViewer.ImageViewer;
import com.squareup.picasso.Picasso;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class Item_overview extends AppCompatActivity {

    EditText quantityET;
    TextView name;
    TextView price;
    String size;
    Spinner sizeSpinner;
    Integer quantity = 1;
    String imageURL;
    String strBase64;

    static ImageView iV;

    byte[] b;

    BasketDbHelper basketDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_overview);

        name = (TextView) findViewById(R.id.nameTV);
        price = (TextView) findViewById(R.id.priceTV);
        iV = (ImageView) findViewById(R.id.imageView2);
        sizeSpinner = (Spinner) findViewById(R.id.spinner);
        quantityET = (EditText) findViewById(R.id.quantityTV);
        TextView increaseTV = (TextView) findViewById(R.id.plus);
        TextView decreaseTV = (TextView) findViewById(R.id.minus);

        quantityET.setText("1");

        Intent intent = getIntent();

        imageURL = intent.getStringExtra("image");
        Picasso.with(this)
                .load(imageURL)
                .placeholder(R.drawable.mocheeloading)
                .into(iV);

        name.setText(intent.getStringExtra("name"));

        iV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( imageURL != null) {
                    Intent intent = new Intent(Item_overview.this, ImageViewer.class);
                    intent.putExtra("image", imageURL);
                    startActivity(intent);
                }
            }
        });

        price.setText(intent.getStringExtra("price"));

        addListenerOnSpinnerItemSelection();

        increaseTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseQuantity();
            }
        });

        decreaseTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseQuantity();
            }
        });

        quantityET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (quantityET.getText().toString().equals("")){
                    return;
                }
                if (parseInt(quantityET.getText().toString()) <= 0){
                    quantityET.setText("1");
                }
            }
        });

    }

    public void submit(View view) {
        if (quantityET.getText().toString().equals("")) {
            Toast.makeText(this, "No quantity selected", Toast.LENGTH_SHORT).show();
        }

        if(CheckIsDataAlreadyInDBorNot()){
            updateProduct();
        }else{
            insertProduct();
        }

        new AlertDialog.Builder(this)
                .setTitle("Item added to cart?")
                .setMessage("Open cart?")
                .setNegativeButton("Return to Items", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Item_overview.super.onBackPressed();
                        finish();
                        getFragmentManager().popBackStackImmediate();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Item_overview.this, BasketActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).create().show();

    }

    private void increaseQuantity(){
        quantity = parseInt(quantityET.getText().toString());
        quantity = quantity + 1;
        quantityET.setText(quantity.toString());
    }

    private void decreaseQuantity(){
        quantity = parseInt(quantityET.getText().toString());
        if (quantity == 1){

        }else{
            quantity = quantity - 1;
            quantityET.setText(quantity.toString());
//                    (""+quantity);
        }
    }

    private void insertProduct(){

        ContentValues values = new ContentValues();

//        iV.buildDrawingCache();
//        Bitmap bmap = iV.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//        b = baos.toByteArray();
//        strBase64= Base64.encodeToString(b, 0);

        values.put(ItemEntry.COLUMN_ITEM_IMAGE, imageURL);
        values.put(ItemEntry.COLUMN_ITEM_NAME, name.getText().toString());
        values.put(ItemEntry.COLUMN_ITEM_PRICE, parseFloat((price.getText().toString()).substring((price.getText().toString()).length() - 6)));
        values.put(ItemEntry.COLUMN_ITEM_SIZE, size);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(ItemEntry.COLUMN_ITEM_TOTALPRICE, (quantity * (parseFloat((price.getText().toString()).substring((price.getText().toString()).length() - 6)))));

        getContentResolver().insert(ItemEntry.CONTENT_URI, values);

    }

    public boolean CheckIsDataAlreadyInDBorNot() {

        String[] columns = { ItemEntry.COLUMN_ITEM_NAME , ItemEntry.COLUMN_ITEM_SIZE , ItemEntry.COLUMN_ITEM_QUANTITY};
        String selection = ItemEntry.COLUMN_ITEM_NAME  + " =? AND " + ItemEntry.COLUMN_ITEM_SIZE + " =?";
        String[] selectionArgs = { name.getText().toString(), size};

        Cursor cursor = getContentResolver().query(ItemEntry.CONTENT_URI,columns,selection,selectionArgs,null);

        int quan = 0;
        if (!cursor.moveToFirst()){
            cursor.moveToFirst();
        }
        try {
            quan = cursor.getInt(cursor.getColumnIndexOrThrow(ItemEntry.COLUMN_ITEM_QUANTITY));
        }catch (Exception e){
            System.out.println("error");
        }
        boolean exists = (cursor.getCount() > 0);

        cursor.close();

        return exists;

    }

    private void updateProduct(){

        String[] columns = { ItemEntry.COLUMN_ITEM_NAME , ItemEntry.COLUMN_ITEM_SIZE , ItemEntry.COLUMN_ITEM_QUANTITY, ItemEntry._ID, ItemEntry.COLUMN_ITEM_TOTALPRICE};
        String selection = ItemEntry.COLUMN_ITEM_NAME  + " =? AND " + ItemEntry.COLUMN_ITEM_SIZE + " =?";
        String[] selectionArgs = { name.getText().toString(), size};

        Cursor cursor = getContentResolver().query(ItemEntry.CONTENT_URI,columns,selection,selectionArgs,null);

        if (!cursor.moveToFirst()){
            cursor.moveToFirst();
        }

        String ID = cursor.getString(cursor.getColumnIndexOrThrow(ItemEntry._ID));

        quantity = quantity + cursor.getInt(cursor.getColumnIndexOrThrow(ItemEntry.COLUMN_ITEM_QUANTITY));

        ContentValues values = new ContentValues();


//        values.put(ItemEntry.COLUMN_ITEM_IMAGE, strBase64);
//        values.put(ItemEntry.COLUMN_ITEM_NAME, name.getText().toString());
//        values.put(ItemEntry.COLUMN_ITEM_PRICE, parseFloat((price.getText().toString()).substring((price.getText().toString()).length() - 6)));
//        values.put(ItemEntry.COLUMN_ITEM_SIZE, size);
        values.put(ItemEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(ItemEntry.COLUMN_ITEM_TOTALPRICE, (quantity * (parseFloat((price.getText().toString()).substring((price.getText().toString()).length() - 6)))));

        getContentResolver().update(ItemEntry.CONTENT_URI,values,ItemEntry._ID + " =? ", new String[]{ID});
        cursor.close();
    }

    public void addListenerOnSpinnerItemSelection() {
        sizeSpinner = (Spinner) findViewById(R.id.spinner);
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id) {
                size = parentView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

}
