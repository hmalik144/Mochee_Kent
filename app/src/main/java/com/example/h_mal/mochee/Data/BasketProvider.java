package com.example.h_mal.mochee.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.h_mal.mochee.Data.BasketContract.ItemEntry;

/**
 * Created by h_mal on 12/01/2018.
 */

public class BasketProvider extends ContentProvider {


    public static final String LOG_TAG = BasketProvider.class.getSimpleName();

    private static final int BASKET = 100;
    private static final int BASKET_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


    static {

        sUriMatcher.addURI(BasketContract.CONTENT_AUTHORITY, BasketContract.PATH_ITEMS, BASKET);

        sUriMatcher.addURI(BasketContract.CONTENT_AUTHORITY, BasketContract.PATH_ITEMS + "/#", BASKET_ID);
    }

    BasketDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new BasketDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case BASKET:

                cursor = database.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BASKET_ID:

                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BASKET:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {

        String image = values.getAsString(ItemEntry.COLUMN_ITEM_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException("Image required");
        }

        String name = values.getAsString(ItemEntry.COLUMN_ITEM_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Name required");
        }

        Float price = values.getAsFloat(ItemEntry.COLUMN_ITEM_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Price required");
        }

        String size = values.getAsString(ItemEntry.COLUMN_ITEM_SIZE);
        if (size == null) {
            throw new IllegalArgumentException("Size In required");
        }

        Integer quantity = values.getAsInteger(ItemEntry.COLUMN_ITEM_QUANTITY);
        if (quantity == null) {
            throw new IllegalArgumentException("Time Out required");
        }

        values.getAsFloat(ItemEntry.COLUMN_ITEM_TOTALPRICE);

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(ItemEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "row failed " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BASKET:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case BASKET_ID:

                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ItemEntry.COLUMN_ITEM_IMAGE)) {
            String image = values.getAsString(ItemEntry.COLUMN_ITEM_IMAGE);
            if (image == null) {
                throw new IllegalArgumentException("image required");
            }
        }

        if (values.containsKey(ItemEntry.COLUMN_ITEM_NAME)) {
            String name = values.getAsString(ItemEntry.COLUMN_ITEM_NAME);
            if (name == null) {
                throw new IllegalArgumentException("name required");
            }
        }

        if (values.containsKey(ItemEntry.COLUMN_ITEM_PRICE)) {
            Float price = values.getAsFloat(ItemEntry.COLUMN_ITEM_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("price required");
            }
        }

        if (values.containsKey(ItemEntry.COLUMN_ITEM_SIZE)) {
            String size = values.getAsString(ItemEntry.COLUMN_ITEM_SIZE);
            if (size == null) {
                throw new IllegalArgumentException("size required");
            }
        }

        if (values.containsKey(ItemEntry.COLUMN_ITEM_QUANTITY)) {
            Integer quantity = values.getAsInteger(ItemEntry.COLUMN_ITEM_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("quantity required");
            }
        }

        if (values.containsKey(ItemEntry.COLUMN_ITEM_TOTALPRICE)) {
            Float totalprice = values.getAsFloat(ItemEntry.COLUMN_ITEM_TOTALPRICE);
            if (totalprice == null) {
                throw new IllegalArgumentException("break required");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BASKET:
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BASKET_ID:
                selection = ItemEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BASKET:
                return ItemEntry.CONTENT_LIST_TYPE;
            case BASKET_ID:
                return ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
