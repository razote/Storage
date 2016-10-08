package com.example.android.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.storage.data.StorageContract.InventoryEntry;
import com.example.android.storage.data.StorageDbHelper;

public class CatalogActivity extends AppCompatActivity {

    private StorageDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        mDbHelper = new StorageDbHelper(this);

        displayDatabaseInfo();
    }

    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        StorageDbHelper mDbHelper = new StorageDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + InventoryEntry.TABLE_NAME, null);
        try {
            // Display the number of rows in the Cursor (which reflects the number of rows in the
            // pets table in the database).
            TextView displayView = (TextView) findViewById(R.id.text_view_test);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());
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
