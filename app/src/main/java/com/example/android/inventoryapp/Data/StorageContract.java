package com.example.android.inventoryapp.Data;

import android.provider.BaseColumns;

public final class StorageContract {

    private StorageContract (){
    }

    public static final class StorageEntry implements BaseColumns {
        public final static String TABLE_NAME = "pantry";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCT_NAME = "name";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_QUANTITY = "quantity";
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";
        public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";
    }
}