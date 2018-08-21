package com.example.android.inventoryapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.inventoryapp.Data.StorageContract.StorageEntry;

public class StorageProvider extends ContentProvider {

    public static final String LOG_TAG = StorageProvider.class.getSimpleName();

    private static final int PANTRY = 100;
    private static final int PANTRY_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
    sUriMatcher.addURI(StorageContract.CONTENT_AUTHORITY, StorageContract.PATH_PANTRY, PANTRY);
    sUriMatcher.addURI(StorageContract.CONTENT_AUTHORITY, StorageContract.PATH_PANTRY + "/#", PANTRY_ID);
    }

    private StorageDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new StorageDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match){
            case PANTRY:
                cursor = database.query(StorageEntry.TABLE_NAME, projection,selection, selectionArgs,
                        null,null, sortOrder);
                break;
            case PANTRY_ID:
                selection = StorageEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StorageEntry.TABLE_NAME, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("Cannot query Unknown URI" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case PANTRY:
                return insertProduct(uri,contentValues);
                default:
                    throw new IllegalArgumentException("Not supported for " + uri);
        }
    }

    private Uri insertProduct (Uri uri, ContentValues values){
        String product = values.getAsString(StorageEntry.COLUMN_PRODUCT_NAME);
        if (product == null){
            throw new IllegalArgumentException("Product name required");
        }
String price = values.getAsString(StorageEntry.COLUMN_PRICE);
        if (price == null){
            throw new IllegalArgumentException("Please insert price");
        }

        Integer quantity = values.getAsInteger(StorageEntry.COLUMN_QUANTITY);
        if (quantity == null || quantity < 0){
            throw new IllegalArgumentException("A valid quantity is required. Quantity cannot be less than 0");
        }

        String supplierName = values.getAsString(StorageEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName == null){
            throw new IllegalArgumentException("Insert supplier name");
        }

        String supplierPhone = values.getAsString(StorageEntry.COLUMN_SUPPLIER_PHONE);
        if (supplierPhone == null){
            throw new IllegalArgumentException("Insert supplier phone number");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(StorageEntry.TABLE_NAME, null, values);
        if (id == -1){
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match  = sUriMatcher.match(uri);
        switch (match){
            case PANTRY:
                return updateProduct(uri,contentValues, selection, selectionArgs);
            case PANTRY_ID:
                selection = StorageEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri,contentValues,selection,selectionArgs);
                default:
                    throw new IllegalArgumentException("Update is not Supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(StorageEntry.COLUMN_PRODUCT_NAME)){
            String product = values.getAsString(StorageEntry.COLUMN_PRODUCT_NAME);
            if (product == null){
                throw new IllegalArgumentException("Product name required");
            }
        }

        if(values.containsKey(StorageEntry.COLUMN_PRICE)){
            String price = values.getAsString(StorageEntry.COLUMN_PRICE);
            if(price == null){
                throw new IllegalArgumentException("Please insert price");
            }
        }

        if(values.containsKey(StorageEntry.COLUMN_QUANTITY)){
            Integer quantity = values.getAsInteger(StorageEntry.COLUMN_QUANTITY);
            if (quantity == null || quantity < 0){
                throw new IllegalArgumentException("A valid quantity is required. Quantity cannot be less than 0");
            }
        }

        if (values.containsKey(StorageEntry.COLUMN_SUPPLIER_NAME)){
            String supplierName = values.getAsString(StorageEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null){
                throw new IllegalArgumentException("Insert supplier name");
            }
        }

        if (values.containsKey(StorageEntry.COLUMN_SUPPLIER_PHONE)){
            String supplierPhone = values.getAsString(StorageEntry.COLUMN_SUPPLIER_PHONE);
            if (supplierPhone == null){
                throw new IllegalArgumentException("Insert supplier phone number");
            }
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(StorageEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int mstch = sUriMatcher.match(uri);
        switch (mstch) {
            case PANTRY:
                rowsDeleted = database.delete(StorageEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PANTRY_ID:
                selection = StorageEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(StorageEntry.TABLE_NAME, selection, selectionArgs);
                break;
                default:
                    throw new IllegalArgumentException("Deletion is not supported for" + uri);
        }

        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case PANTRY:
                return StorageEntry.CONTENT_LIST_TYPE;
            case PANTRY_ID:
                return StorageEntry.CONTENT_ITEM_TYPE;
                default:
                    throw new IllegalArgumentException("Unknown URI" + uri + "with match" + match);
        }
    }
}