package com.example.android.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventory.data.Contract.InventoryEntry;

public class DbHelper extends SQLiteOpenHelper {

    /** Name of the database file */
    private static final String DATABASE_NAME = "inventory.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link DbHelper}.
     *
     * @param context of the app
     */
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " (" +
                InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InventoryEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                InventoryEntry.COLUMN_PRICE + " INTEGER NOT NULL, " +
                InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL DEFAULT 0, " +
                InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT, " +
                InventoryEntry.COLUMN_SUPPLIER_PHONE + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
