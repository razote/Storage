package com.example.android.storage.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static android.transition.Fade.IN;

public class StorageProvider extends ContentProvider {

    /** Tag for the log messages */
    public static final String LOG_TAG = StorageProvider.class.getSimpleName();

    private StorageDbHelper mDbHelper;

    private static final int INVENTORY = 100;
    private static final int INVENTORY_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(StorageContract.CONTENT_AUTHORITY, StorageContract.PATH_INVENTORY, INVENTORY);
        sUriMatcher.addURI(StorageContract.CONTENT_AUTHORITY, StorageContract.PATH_INVENTORY + "/#", INVENTORY_ID);
    }

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new StorageDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                cursor = database.query(StorageContract.InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case INVENTORY_ID:
                selection = StorageContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(StorageContract.InventoryEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertInventory(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertInventory(Uri uri, ContentValues values) {
        String name = values.getAsString(StorageContract.InventoryEntry.COLUMN_INVETORY_NAME);
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Inventory requires a name");
        }

        Integer quantity = values.getAsInteger(StorageContract.InventoryEntry.COLUMN_INVETORY_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Inventory requires valid quantity");
        }

        Integer price = values.getAsInteger(StorageContract.InventoryEntry.COLUMN_INVETORY_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Inventory requires valid price");
        }

        String img_dir = values.getAsString(StorageContract.InventoryEntry.COLUMN_INVETORY_IMG_DIR);
        if (img_dir == null || img_dir.isEmpty()) {
            throw new IllegalArgumentException("Inventory requires an image");
        }

        Integer sell = values.getAsInteger(StorageContract.InventoryEntry.COLUMN_INVETORY_SELLABLE);
        if (sell == null || !StorageContract.InventoryEntry.isValidSellable(sell)) {
            throw new IllegalArgumentException("Inventory requires valid price");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(StorageContract.InventoryEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return updateInventory(uri, contentValues, selection, selectionArgs);
            case INVENTORY_ID:
                // For the INVENTORY_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = StorageContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateInventory(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateInventory(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(StorageContract.InventoryEntry.COLUMN_INVETORY_NAME)) {
            String name = values.getAsString(StorageContract.InventoryEntry.COLUMN_INVETORY_NAME);
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("Inventory requires a name");
            }
        }

        if (values.containsKey(StorageContract.InventoryEntry.COLUMN_INVETORY_QUANTITY)) {
            Integer quantity = values.getAsInteger(StorageContract.InventoryEntry.COLUMN_INVETORY_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Inventory requires valid quantity");
            }
        }

        if (values.containsKey(StorageContract.InventoryEntry.COLUMN_INVETORY_PRICE)) {
            Integer price = values.getAsInteger(StorageContract.InventoryEntry.COLUMN_INVETORY_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Inventory requires valid price");
            }
        }

        if (values.containsKey(StorageContract.InventoryEntry.COLUMN_INVETORY_IMG_DIR)) {
            String img_dir = values.getAsString(StorageContract.InventoryEntry.COLUMN_INVETORY_IMG_DIR);
            if (img_dir == null || img_dir.isEmpty()) {
                throw new IllegalArgumentException("Inventory requires an image");
            }
        }

        if (values.containsKey(StorageContract.InventoryEntry.COLUMN_INVETORY_SELLABLE)) {
            Integer sell = values.getAsInteger(StorageContract.InventoryEntry.COLUMN_INVETORY_SELLABLE);
            if (sell == null || !StorageContract.InventoryEntry.isValidSellable(sell)) {
                throw new IllegalArgumentException("Inventory requires valid price");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        return database.update(StorageContract.InventoryEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return database.delete(StorageContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
            case INVENTORY_ID:
                selection = StorageContract.InventoryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return database.delete(StorageContract.InventoryEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }

}
