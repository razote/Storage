package com.example.android.storage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.storage.data.StorageContract.InventoryEntry;

public class EditorActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mQuantity;
    private EditText mPrice;
    private EditText mImageDir;
    private Spinner mSellableSpinner;
    private int mSellable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mName = (EditText) findViewById(R.id.edit_text_name);
        mQuantity = (EditText) findViewById(R.id.edit_text_quantity);
        mPrice = (EditText) findViewById(R.id.edit_text_price);
        mImageDir = (EditText) findViewById(R.id.edit_text_img_dir);
        mSellableSpinner = (Spinner) findViewById(R.id.spinner_sellable);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter sellableSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_boolean_option, android.R.layout.simple_spinner_item);

        sellableSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSellableSpinner.setAdapter(sellableSpinnerAdapter);

        mSellableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.boolean_true))) {
                        mSellable = InventoryEntry.SELLABLE_TRUE;
                    } else {
                        mSellable = InventoryEntry.SELLABLE_FALSE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSellable = InventoryEntry.SELLABLE_TRUE;
            }
        });
    }
}
