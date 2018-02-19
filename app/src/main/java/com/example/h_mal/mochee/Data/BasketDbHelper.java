package com.example.h_mal.mochee.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.h_mal.mochee.Data.BasketContract.ItemEntry;

/**
 * Created by h_mal on 12/01/2018.
 */

public class BasketDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BasketDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "items.db";

    private static final int DATABASE_VERSION = 1;


    public BasketDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_PRODUCTS_TABLE =  "CREATE TABLE " + ItemEntry.TABLE_NAME + " ("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_ITEM_IMAGE + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_PRICE + " FLOAT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL, "
                + ItemEntry.COLUMN_ITEM_SIZE + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_ITEM_TOTALPRICE + " FLOAT NOT NULL DEFAULT 0)";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
