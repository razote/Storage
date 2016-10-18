package com.example.android.storage;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.storage.data.StorageContract.InventoryEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_INVENTORY_LOADER = 0;
    private Uri mCurrentInventoryUri;

    private EditText mName;
    private EditText mQuantity;
    private EditText mPrice;
    private EditText mImageDir;
    private Spinner mSellSpinner;
    private int mSellVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentInventoryUri = intent.getData();

        if (mCurrentInventoryUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_item));
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_item));
            getLoaderManager().initLoader(EXISTING_INVENTORY_LOADER, null, this);
        }

        mName = (EditText) findViewById(R.id.edit_text_name);
        mQuantity = (EditText) findViewById(R.id.edit_text_quantity);
        mPrice = (EditText) findViewById(R.id.edit_text_price);
        mImageDir = (EditText) findViewById(R.id.edit_text_img_dir);
        mSellSpinner = (Spinner) findViewById(R.id.spinner_sellable);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter sellableSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_boolean_option, android.R.layout.simple_spinner_item);

        sellableSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSellSpinner.setAdapter(sellableSpinnerAdapter);

        mSellSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.boolean_true))) {
                        mSellVal = InventoryEntry.SELLABLE_TRUE;
                    } else {
                        mSellVal = InventoryEntry.SELLABLE_FALSE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSellVal = InventoryEntry.SELLABLE_TRUE;
            }
        });
    }

    private void saveInventory() {
        String nameString = mName.getText().toString().trim();
        String quantityString = mQuantity.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();
        String img_dirString = mImageDir.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        int price = Integer.parseInt(priceString);

        ContentValues values = new ContentValues();

        values.put(InventoryEntry.COLUMN_INVETORY_NAME, nameString);
        values.put(InventoryEntry.COLUMN_INVETORY_QUANTITY, quantity);
        values.put(InventoryEntry.COLUMN_INVETORY_PRICE, price);
        values.put(InventoryEntry.COLUMN_INVETORY_IMG_DIR, img_dirString);
        values.put(InventoryEntry.COLUMN_INVETORY_SELLABLE, mSellVal);

        if (mCurrentInventoryUri == null) {
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Error in saving Inventory", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Inventory Saved", Toast.LENGTH_SHORT).show();
            }

        } else {
            int rowsAffected = getContentResolver().update(mCurrentInventoryUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_inventory_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_inventory_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveInventory();
                finish();
                return true;
            case R.id.action_delete:
                //TODO
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INVETORY_NAME,
                InventoryEntry.COLUMN_INVETORY_QUANTITY,
                InventoryEntry.COLUMN_INVETORY_PRICE,
                InventoryEntry.COLUMN_INVETORY_IMG_DIR,
                InventoryEntry.COLUMN_INVETORY_SELLABLE };

        return new CursorLoader(this,   // Parent activity context
                mCurrentInventoryUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_PRICE);
            int imgDirColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_IMG_DIR);
            int sellColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INVETORY_SELLABLE);

            String name = cursor.getString(nameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            String imgDir = cursor.getString(imgDirColumnIndex);
            int sell = cursor.getInt(sellColumnIndex);

            mName.setText(name);
            mQuantity.setText(Integer.toString(quantity));
            mPrice.setText(Integer.toString(price));
            mImageDir.setText(imgDir);

            switch (sell) {
                case InventoryEntry.SELLABLE_TRUE:
                    mSellSpinner.setSelection(0);
                    break;
                case InventoryEntry.SELLABLE_FALSE:
                    mSellSpinner.setSelection(1);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mName.setText("");
        mQuantity.setText(Integer.toString(0));
        mPrice.setText(Integer.toString(0));
        mImageDir.setText("");
        mSellSpinner.setSelection(0);
    }
}
