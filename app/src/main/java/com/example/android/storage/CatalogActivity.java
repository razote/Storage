package com.example.android.storage;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.storage.data.StorageContract.InventoryEntry;
import com.example.android.storage.data.StorageDbHelper;

public class CatalogActivity extends AppCompatActivity {

    private StorageDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new StorageDbHelper(this);

        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVETORY_NAME,
                InventoryEntry.COLUMN_INVETORY_QUANTITY,
                InventoryEntry.COLUMN_INVETORY_PRICE,
                InventoryEntry.COLUMN_INVETORY_IMG_DIR,
                InventoryEntry.COLUMN_INVETORY_SELLABLE
        };

        Cursor cursor = db.query(
                InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayView = (TextView) findViewById(R.id.text_view_test);

        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).

//            displayView.setText("Number of rows in pets database table: " + cursor.getCount());

            displayView.setText("The Inventory table contains " + cursor.getCount() + " items.\n\n");

            displayView.append(InventoryEntry._ID + " - " +
                    InventoryEntry.COLUMN_INVETORY_NAME + " - " +
                    InventoryEntry.COLUMN_INVETORY_QUANTITY + " - " +
                    InventoryEntry.COLUMN_INVETORY_PRICE + " - " +
                    InventoryEntry.COLUMN_INVETORY_IMG_DIR + " - " +
                    InventoryEntry.COLUMN_INVETORY_SELLABLE + "\n");

            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_PRICE);
            int imgDirColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_IMG_DIR);
            int sellColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_SELLABLE);

            while(cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                String currentImgDir = cursor.getString(imgDirColumnIndex);
                int currentSell = cursor.getInt(sellColumnIndex);

                displayView.append(("\n" + currentId + " - " +
                        currentName + " - " +
                        currentQuantity + " - " +
                        currentPrice + " - " +
                        currentImgDir + " - " +
                        currentSell));
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    private void insertInventory() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(InventoryEntry.COLUMN_INVETORY_NAME, "Shirt");
        values.put(InventoryEntry.COLUMN_INVETORY_QUANTITY, 1);
        values.put(InventoryEntry.COLUMN_INVETORY_PRICE, 300);
        values.put(InventoryEntry.COLUMN_INVETORY_IMG_DIR, "Test_Dir");
        values.put(InventoryEntry.COLUMN_INVETORY_SELLABLE, InventoryEntry.SELLABLE_TRUE);

        long createdRow = db.insert(InventoryEntry.TABLE_NAME, null, values);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.insert_dummy_data:
                insertInventory();
                displayDatabaseInfo();
                break;
            case R.id.delete_all_entries:
                //TODO
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
