package com.example.android.storage;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.android.storage.data.StorageDbHelper;

public class EditorActivity extends AppCompatActivity {

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

    private void insertInventory() {
        String nameString = mName.getText().toString().trim();
        String quantityString = mQuantity.getText().toString().trim();
        String priceString = mPrice.getText().toString().trim();
        String img_dirString = mImageDir.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        int price = Integer.parseInt(priceString);

        //StorageDbHelper mDbHelper = new StorageDbHelper(this);

        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(InventoryEntry.COLUMN_INVETORY_NAME, nameString);
        values.put(InventoryEntry.COLUMN_INVETORY_QUANTITY, quantity);
        values.put(InventoryEntry.COLUMN_INVETORY_PRICE, price);
        values.put(InventoryEntry.COLUMN_INVETORY_IMG_DIR, img_dirString);
        values.put(InventoryEntry.COLUMN_INVETORY_SELLABLE, mSellVal);

        //long createdRow = db.insert(InventoryEntry.TABLE_NAME, null, values);
        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

        if(newUri == null) {
            Toast.makeText(this, "Error in saving Inventory", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Inventory Saved", Toast.LENGTH_SHORT).show();
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
                insertInventory();
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
}
