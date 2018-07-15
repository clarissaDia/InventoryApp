package com.example.android.inventoryapp;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.inventoryapp.Data.StorageContract.StorageEntry;
import com.example.android.inventoryapp.Data.StorageDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mProductEditText;
    private EditText mPriceEditText;
    private EditText mQuantityEditText;
    private EditText mSupplierNameEditText;
    private EditText mSupplierPhoneEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_activity);

        mProductEditText = findViewById(R.id.edit_product);
        mPriceEditText = findViewById(R.id.edit_price);
        mQuantityEditText = findViewById(R.id.edit_quantity);
        mSupplierNameEditText = findViewById(R.id.edit_supplier_name);
        mSupplierPhoneEditText = findViewById(R.id.edit_supplier_phone);
    }

    private void insertPantry (){
        String productString = mProductEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneString = mSupplierPhoneEditText.getText().toString().trim();

        StorageDbHelper mDbHelper = new StorageDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StorageEntry.COLUMN_PRODUCT_NAME, productString);
        values.put(StorageEntry.COLUMN_PRICE, priceString);
        values.put(StorageEntry.COLUMN_QUANTITY, quantity);
        values.put(StorageEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(StorageEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);

        long newRowId = db.insert(StorageEntry.TABLE_NAME, null, values);

        if (newRowId == -1){
            Toast.makeText(this,"Error saving entry", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Entry saved with row id" + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertPantry();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}