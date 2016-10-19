package com.example.android.storage;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.storage.data.StorageContract;

import java.text.NumberFormat;

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
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);

        int nameColumnIndex = cursor.getColumnIndex(StorageContract.InventoryEntry.COLUMN_INVETORY_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(StorageContract.InventoryEntry.COLUMN_INVETORY_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(StorageContract.InventoryEntry.COLUMN_INVETORY_PRICE);

        String inventoryName = cursor.getString(nameColumnIndex);
        int inventoryQuantity = cursor.getInt(quantityColumnIndex);
        int inventoryPrice = cursor.getInt(priceColumnIndex);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(inventoryPrice);

        nameTextView.setText(inventoryName);
        quantityTextView.setText("Quantity: " + Integer.toString(inventoryQuantity));
        priceTextView.setText(moneyString);
    }
}
