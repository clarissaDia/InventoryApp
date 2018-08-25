package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.Data.StorageContract.StorageEntry;

public class StorageCursorAdapter extends CursorAdapter{

    public StorageCursorAdapter (Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView productTextView = (TextView) view.findViewById(R.id.product);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        Button saleButton = (Button) view.findViewById(R.id.sale_button);

        int productColumnIndex = cursor.getColumnIndex(StorageEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(StorageEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(StorageEntry.COLUMN_QUANTITY);

        String productName = cursor.getString(productColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);

        productTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);

        final int quantity = Integer.valueOf(productQuantity);
        final int currentProductId = cursor.getInt(cursor.getColumnIndex(StorageEntry._ID));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0){
                    int decrementQuantity = quantity -1;
                    Uri quantityUri = ContentUris.withAppendedId(StorageEntry.CONTENT_URI, currentProductId);
                    ContentValues values = new ContentValues();
                    values.put(StorageEntry.COLUMN_QUANTITY, decrementQuantity);
                    context.getContentResolver().update(quantityUri, values, null,null);
                }else {
                    Toast.makeText(context, R.string.out_stock,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
