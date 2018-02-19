package com.example.h_mal.mochee;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.h_mal.mochee.Data.BasketContract;

import java.io.File;

/**
 * Created by h_mal on 19/01/2018.
 */

public class Basket_Counter {

    private static int total;

    private Basket_Counter(){}

    public static int setBasketQuantity(Context context){
        total = 0;
        try {
            File dbFile = context.getDatabasePath("items.db");

            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.CREATE_IF_NECESSARY);
            Cursor cursor = db.rawQuery("SELECT SUM(" + BasketContract.ItemEntry.COLUMN_ITEM_QUANTITY + ") AS Total FROM " + BasketContract.ItemEntry.TABLE_NAME + ";", null);


            if (cursor.moveToFirst()) {
                total = cursor.getInt(cursor.getColumnIndex("Total"));// get final subTotalVal
            }
            cursor.close();

        }catch(Exception e){
            total = 0;
        }

        return total;
    }
}
