package com.example.android.inventoryapp;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.example.android.inventoryapp.Data.StorageContract.StorageEntry;
import com.example.android.inventoryapp.Data.StorageDbHelper;
public class CatalogActivity extends AppCompatActivity {

    private StorageDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new StorageDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                StorageEntry._ID,
                StorageEntry.COLUMN_PRODUCT_NAME,
                StorageEntry.COLUMN_PRICE,
                StorageEntry.COLUMN_QUANTITY,
                StorageEntry.COLUMN_SUPPLIER_NAME,
                StorageEntry.COLUMN_SUPPLIER_PHONE,};

        Cursor cursor = db.query(StorageEntry.TABLE_NAME, projection,
                null, null, null, null, null);

        TextView displayView = (TextView) findViewById(R.id.text_view_storage);

        try {
            displayView.setText("The table contains" + cursor.getCount() + "product in the pantry.\n\n");
            displayView.append(StorageEntry._ID + "-" +
                    StorageEntry.COLUMN_PRODUCT_NAME + "-" +
                    StorageEntry.COLUMN_PRICE + "-" +
                    StorageEntry.COLUMN_QUANTITY + "-" +
                    StorageEntry.COLUMN_SUPPLIER_NAME + "-" +
                    StorageEntry.COLUMN_SUPPLIER_PHONE + "\n");

            int idColumnIndex = cursor.getColumnIndex(StorageEntry._ID);
            int productColumnIndex = cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(StorageEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(StorageEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(StorageEntry.COLUMN_SUPPLIER_NAME);
            int suplierPhoneColumnIndex = cursor.getColumnIndex(StorageEntry.COLUMN_SUPPLIER_PHONE);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentProduct = cursor.getString(productColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(suplierPhoneColumnIndex);

                displayView.append(("\n" + currentID + "-" +
                        currentProduct + "-" +
                        currentPrice + "-" +
                        currentQuantity + "-" +
                        currentSupplier + "-" +
                        currentSupplierPhone));
            }
        }finally {
            cursor.close();
        }
        }
    }