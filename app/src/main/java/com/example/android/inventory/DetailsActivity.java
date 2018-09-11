package com.example.android.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.data.Contract.InventoryEntry;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_INVENTORY_LOADER = 0;

    private Uri mCurrentInventoryUri;

    private TextView productNameTextView;
    private TextView priceTextView;
    private TextView quantityTextView;
    private TextView supplierNameTextView;
    private TextView supplierPhoneTextView;
    private int currentQuantity;
    private String currentSupplierPhone;
    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();

        setTitle(getString(R.string.details_activity_title));

        invalidateOptionsMenu();

        productNameTextView = findViewById(R.id.details_product_name);
        priceTextView = findViewById(R.id.details_price);
        quantityTextView = findViewById(R.id.details_quantity);
        supplierNameTextView = findViewById(R.id.details_supplier_name);
        supplierPhoneTextView = findViewById(R.id.details_supplier_phone);
        Button quantityIncreaseButton = findViewById(R.id.details_increase_quantity);
        Button quantityDecreaseButton = findViewById(R.id.details_decrease_quantity);
        Button callSupplierButton = findViewById(R.id.details_call_supplier);

        getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);

        callSupplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:" + currentSupplierPhone));
                startActivity(intent1);
            }
        });

        quantityDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues values = new ContentValues();

                if(currentQuantity > 0){
                    values.put(InventoryEntry.COLUMN_QUANTITY, --currentQuantity);
                    Toast.makeText(getApplicationContext(), "Quantity decreased", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Already zero", Toast.LENGTH_SHORT).show();
                }

                getApplicationContext().getContentResolver().update(mCurrentInventoryUri, values, null, null);

                //quantityTextView.setText("Quantity: " + String.valueOf(currentQuantity));
            }
        });
        quantityIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues values = new ContentValues();

                values.put(InventoryEntry.COLUMN_QUANTITY, ++currentQuantity);
                Toast.makeText(getApplicationContext(), "Quantity increased", Toast.LENGTH_SHORT).show();

                getApplicationContext().getContentResolver().update(mCurrentInventoryUri, values, null, null);

                //quantityTextView.setText("Quantity: " + String.valueOf(currentQuantity));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(DetailsActivity.this, EditorActivity.class);

                Uri currentUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, currentId);

                intent.setData(currentUri);

                startActivity(intent);
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {InventoryEntry._ID, InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE, InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME, InventoryEntry.COLUMN_SUPPLIER_PHONE};

        return new CursorLoader(this, mCurrentInventoryUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);
            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);

            String productName = cursor.getString(productNameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            currentQuantity = quantity;
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
            currentSupplierPhone = supplierPhone;
            int id = cursor.getInt(idColumnIndex);
            currentId = id;

            productNameTextView.setText("Item name: " + productName);
            priceTextView.setText("$" + String.valueOf(((float)price / 100)));
            quantityTextView.setText("Quantity: " + Integer.toString(quantity));
            supplierNameTextView.setText("Supplier name: " + supplierName);
            supplierPhoneTextView.setText("Supplier phone: " + supplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productNameTextView.setText("");
        priceTextView.setText("");
        quantityTextView.setText("");
        supplierNameTextView.setText("");
        supplierPhoneTextView.setText("");
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        if (mCurrentInventoryUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentInventoryUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}
