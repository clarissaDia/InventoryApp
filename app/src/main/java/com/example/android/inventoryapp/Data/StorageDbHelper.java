package com.example.android.inventoryapp.Data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventoryapp.Data.StorageContract.StorageEntry;

public class StorageDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "kitchenpantry.db";
    public static final int DATABASE_VERSION = 1;

    public StorageDbHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PANTRY_TABLE = "CREATE TABLE " + StorageEntry.TABLE_NAME + " ("
                + StorageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StorageEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + StorageEntry.COLUMN_PRICE + " TEXT NOT NULL, "
                + StorageEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + StorageEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + StorageEntry.COLUMN_SUPPLIER_PHONE + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PANTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}