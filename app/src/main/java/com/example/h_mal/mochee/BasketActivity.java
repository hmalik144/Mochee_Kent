package com.example.h_mal.mochee;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.h_mal.mochee.Data.BasketContract.ItemEntry;
import com.example.h_mal.mochee.Data.BasketCursorAdapter;
import com.example.h_mal.mochee.Data.BasketDbHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BasketActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int BASKET_LOADER = 0;
    BasketCursorAdapter mCursorAdapter;

    BasketDbHelper mDbHelper;
    CursorLoader cursor;
    Double subtotalPrice;
    Double deliveryPrice;
    Double totalPrice;
    Double subTotalVal;

    TextView sum;
    TextView subTotalTV;
    TextView deliveryType;
    TextView deliveryPriceTV;

    EditText promoCodeET;
    EditText customerName;
    EditText customerAddress;
    EditText customerCountry;
    EditText customerEmail;
    EditText addNoteET;
    ProgressBar pb;
    NestedScrollView wholeView;
    LinearLayout newView;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        wholeView = (NestedScrollView) findViewById(R.id.whole_view);
        newView = (LinearLayout) findViewById(R.id.emptyView) ;
        NonScrollListView productListView = (NonScrollListView) findViewById(R.id.list_item_view);
        LinearLayout promoCodeTV = (LinearLayout) findViewById(R.id.EnterPromoCode);
        final LinearLayout promoCodeLayout = (LinearLayout) findViewById(R.id.promocodeLayout);
        promoCodeET = (EditText) findViewById(R.id.promoCodeET);
        Button promoCodeBtn = (Button) findViewById(R.id.promoCodeBtn);
        final LinearLayout addNoteTV = (LinearLayout) findViewById(R.id.addNoteTV);
        addNoteET = (EditText) findViewById(R.id.addNoteET);
        subTotalTV = (TextView) findViewById(R.id.subtotalTV);
        sum = (TextView) findViewById(R.id.sumTotalTV);
        Button button = (Button) findViewById(R.id.button3);
        Button emptyCart = (Button) findViewById(R.id.emptyCart);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);

        customerName = (EditText) findViewById(R.id.detailsName);
        customerAddress = (EditText) findViewById(R.id.detailAddress);
        customerCountry = (EditText) findViewById(R.id.detailCountry);
        customerEmail = (EditText) findViewById(R.id.detailEmail);

        customerCountry.setInputType(InputType.TYPE_NULL);

        final ArrayAdapter<String> spinner_countries = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.countries_array));

        customerCountry.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(BasketActivity.this)
                        .setTitle("Select Countries")
                        .setAdapter(spinner_countries, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                customerCountry.setText(getResources().getStringArray(R.array.countries_array)[which].toString());
                                setDeliveryPrice();
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        deliveryType = (TextView)  findViewById(R.id.deliveryTypeTV);
        deliveryPriceTV = (TextView)  findViewById(R.id.deliveryPriceTV);
        deliveryType.setText("");
        deliveryPriceTV.setText("");
        deliveryPrice = 0.00d;

        emptyCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(BasketActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        SetTitle();

        mCursorAdapter = new BasketCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        getLoaderManager().initLoader(BASKET_LOADER, null, this);

        promoCodeTV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(promoCodeLayout.getVisibility()==View.GONE) {
                    promoCodeLayout.setVisibility(View.VISIBLE);
                }else{
                    promoCodeLayout.setVisibility(View.GONE);
                }
            }
        });

        promoCodeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                promoCodeET.setText("");
                Toast.makeText(BasketActivity.this, "Invalid Promotion Code", Toast.LENGTH_SHORT).show();
            }
        });

        addNoteTV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(addNoteET.getVisibility()==View.GONE) {
                    addNoteET.setVisibility(View.VISIBLE);
                }else{
                    addNoteET.setVisibility(View.GONE);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(TextUtils.isEmpty(customerName.getText()) || TextUtils.isEmpty(customerAddress.getText()) || TextUtils.isEmpty(customerCountry.getText()) || TextUtils.isEmpty(customerCountry.getText()) ){
                    Toast.makeText(BasketActivity.this, "Insert All Mandatory Fields", Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(customerName.getText())){
                        customerName.setHintTextColor(Color.parseColor("#ff0000"));
                    }
                    if(TextUtils.isEmpty(customerAddress.getText())){
                        customerAddress.setHintTextColor(Color.parseColor("#ff0000"));
                    }
                    if(TextUtils.isEmpty(customerCountry.getText())){
                        customerCountry.setHintTextColor(Color.parseColor("#ff0000"));
                    }
                    return;
                }


                PayPalPayment payment = new PayPalPayment(new BigDecimal(sum.getText().toString()), "GBP", "Mochee",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(BasketActivity.this, PaymentActivity.class);

                // send the same configuration for restart resiliency
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

                startActivityForResult(intent, 0);

            }
        });
        setSubTotal();
        setTotal();

        if(Basket_Counter.setBasketQuantity(this) == 0){
        newView.setVisibility(View.VISIBLE);
        wholeView.setVisibility(View.GONE);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                deleteAllProducts();
                SetTitle();
                newView.setVisibility(View.VISIBLE);
                wholeView.setVisibility(View.GONE);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDeliveryPrice(){
        if(customerCountry.getText().toString().equals("United Kingdom")){
            deliveryPrice = 5.99d;
            deliveryPriceTV.setText("£"+deliveryPrice);
            deliveryType.setText("UK Delivery");
            setTotal();
        }else{
            deliveryPrice = 19.99d;
            deliveryPriceTV.setText("£"+deliveryPrice);
            deliveryType.setText("International Delivery");
            setTotal();
        }
    }

    private void SetTitle(){
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Basket(" + (Basket_Counter.setBasketQuantity(this)+"") + ")");
    }

    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ItemEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted");
    }

    private void setSubTotal(){
        try {
            File dbFile = this.getDatabasePath("items.db");

            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);

            Cursor cursor = db.rawQuery("SELECT SUM(" + ItemEntry.COLUMN_ITEM_TOTALPRICE + ") AS Total FROM " + ItemEntry.TABLE_NAME + ";", null);

            subTotalVal = 0.0d;
            if (cursor.moveToFirst()) {
                subTotalVal = cursor.getDouble(cursor.getColumnIndex("Total"));// get final subTotalVal
            }

            subTotalTV.setText("£"+ subTotalVal);
            setTotal();
            cursor.close();
        }
        catch (Exception e){
            subTotalTV.setText("0.00");
        }
    }

    private void setTotal(){
        try{
//        if(deliveryPriceTV != null) {
//            deliveryPrice = Double.parseDouble(deliveryPriceTV.getText().toString());
//        }else{
//            deliveryPrice = 0.0d;
//        }
        totalPrice = subTotalVal + deliveryPrice;

        sum.setText(totalPrice+"");

        }catch(Exception e){
            sum.setText("0");
        }
    }

    private Order createOrder(String transactionID, String transactionDate, String transactionState){
        ArrayList<Item> basket = new ArrayList<Item>();

        Cursor newCurso = getContentResolver().query(ItemEntry.CONTENT_URI,cursor.getProjection(),null,null,null);
        if (newCurso != null && newCurso.moveToFirst()) {
            //add row to list
            do {
                Integer id = newCurso.getInt(newCurso.getColumnIndex(ItemEntry._ID));
                String thisTitle = newCurso.getString(newCurso.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME));
                String thisArtist = newCurso.getString(newCurso.getColumnIndex(ItemEntry.COLUMN_ITEM_SIZE));
                Integer thisAlbum = newCurso.getInt(newCurso.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY));
                basket.add(new Item(id, thisTitle, thisArtist, thisAlbum));
            }
            while (newCurso.moveToNext());
            newCurso.close();
        }

        Price price = new Price(deliveryPrice,
                subtotalPrice,
                totalPrice);
        PaypalInfo paypalInfo = new PaypalInfo(transactionID,transactionDate,transactionState);
        Payment payment = new Payment(price,paypalInfo);

        Order order = null;
        try {
            String email = "";
            if(!TextUtils.isEmpty(customerEmail.getText())) {
                email = customerEmail.getText().toString();
            }
            String note = "";
            if(!TextUtils.isEmpty(addNoteET.getText())){
                note = addNoteET.getText().toString();
            }
            order = new Order(customerName.getText().toString(),
                    customerAddress.getText().toString(),
                    customerCountry.getText().toString(),
                    email,
                    note,
                    payment,
                    basket);

        }catch (Exception e){
            Toast.makeText(this, "missing info", Toast.LENGTH_SHORT).show();
        }
        return order;

    }

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)

            .clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    //this log contains the paypal details
                    confirm.getProofOfPayment().getPaymentId();
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    mDatabase.child("Order").push().setValue(createOrder(confirm.getProofOfPayment().getTransactionId(),confirm.getProofOfPayment().getCreateTime(),confirm.getProofOfPayment().getState()));

                    Config.clearForm((ViewGroup) findViewById(R.id.whole_view));
                    deleteAllProducts();
                    Toast.makeText(this, "Successful payment", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BasketActivity.this, MainActivity.class);
                    startActivity(intent);
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Basket?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        BasketActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_ITEM_IMAGE,
                ItemEntry.COLUMN_ITEM_NAME,
                ItemEntry.COLUMN_ITEM_PRICE,
                ItemEntry.COLUMN_ITEM_SIZE,
                ItemEntry.COLUMN_ITEM_QUANTITY,
                ItemEntry.COLUMN_ITEM_TOTALPRICE};

            cursor = new CursorLoader(this,
                ItemEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

        pb.setVisibility(View.VISIBLE);

        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
        pb.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
        pb.setVisibility(View.VISIBLE);
    }
}
