package com.example.android.inventory;

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

import com.example.android.inventory.data.Contract.InventoryEntry;

public class InventoryCursorAdapter extends CursorAdapter {

    InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final TextView productNameTextView = view.findViewById(R.id.product_name);
        final TextView priceTextView = view.findViewById(R.id.price);
        final TextView quantityTextView = view.findViewById(R.id.quantity);

        int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
        int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);

        final String productName = cursor.getString(productNameColumnIndex);
        final int price = cursor.getInt(priceColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);
        final int id = cursor.getInt(idColumnIndex);

        productNameTextView.setText(productName);
        priceTextView.setText("$" + String.valueOf((float)price / 100));
        quantityTextView.setText("Quantity: " + String.valueOf(quantity));

        Button saleButton = view.findViewById(R.id.sale_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri currentInventoryUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);


                ContentValues values = new ContentValues();

                if(quantity > 0){
                    values.put(InventoryEntry.COLUMN_QUANTITY, quantity - 1);
                    Toast.makeText(context, "Quantity of " + productName + " decreased", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Out of " + productName, Toast.LENGTH_SHORT).show();
                }

                context.getContentResolver().update(currentInventoryUri, values, null, null);

                quantityTextView.setText("Quantity: " + String.valueOf(quantity));
            }
        });
    }
}