package com.example.android.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Inventory app.
 */
public class Contract {
    /**
     * Empty constructor, if someone accidentally instantiates the Contract class.
     */
    private Contract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY_ITEMS = "inventoryItems";

    /**
     * Inner class that defines constant values for the database table.
     * Each entry in the table represents a single item.
     */
    public static final class InventoryEntry implements BaseColumns{

        /** The content URI to access the inventory items data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY_ITEMS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of inventory items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY_ITEMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single inventory item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY_ITEMS;

        /**
         * Name of database table
         */
        public final static String TABLE_NAME = "inventoryItems";

        /**
         * Unique ID number for the item (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME = "product_name";

        /**
         * Price of the product.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRICE = "price";

        /**
         * Quantity of the product.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /**
         * Name of the supplier for this product.
         *
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";

        /**
         * Phone number of the supplier for this product.
         *
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE = "supplier_phone";

    }
}
