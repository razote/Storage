package com.example.android.storage;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.storage.data.StorageContract.InventoryEntry;
import com.example.android.storage.data.StorageDbHelper;

public class CatalogActivity extends AppCompatActivity {

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

        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVETORY_NAME,
                InventoryEntry.COLUMN_INVETORY_QUANTITY,
                InventoryEntry.COLUMN_INVETORY_PRICE,
                InventoryEntry.COLUMN_INVETORY_IMG_DIR,
                InventoryEntry.COLUMN_INVETORY_SELLABLE
        };

        Cursor cursor = getContentResolver().query(InventoryEntry.CONTENT_URI, projection,
                null, null, null);

        ListView inventoryListView = (ListView) findViewById(R.id.list);
        InventoryCursorAdapter adapter = new InventoryCursorAdapter(this, cursor);
        inventoryListView.setAdapter(adapter);
    }

    private void insertInventory() {
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(InventoryEntry.COLUMN_INVETORY_NAME, "Shirt");
        values.put(InventoryEntry.COLUMN_INVETORY_QUANTITY, 1);
        values.put(InventoryEntry.COLUMN_INVETORY_PRICE, 300);
        values.put(InventoryEntry.COLUMN_INVETORY_IMG_DIR, "Test_Dir");
        values.put(InventoryEntry.COLUMN_INVETORY_SELLABLE, InventoryEntry.SELLABLE_TRUE);

        //long createdRow = db.insert(InventoryEntry.TABLE_NAME, null, values);
        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
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
