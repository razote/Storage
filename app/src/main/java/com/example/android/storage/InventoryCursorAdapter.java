package com.example.android.storage;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.storage.data.StorageContract;

public class InventoryCursorAdapter extends CursorAdapter{
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        int nameColumnIndex = cursor.getColumnIndex(StorageContract.InventoryEntry.COLUMN_INVETORY_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(StorageContract.InventoryEntry.COLUMN_INVETORY_QUANTITY);

        String inventoryName = cursor.getString(nameColumnIndex);
        int invetoryQuantity = cursor.getInt(quantityColumnIndex);

        nameTextView.setText(inventoryName);
        summaryTextView.setText(Integer.toString(invetoryQuantity));
    }
}
