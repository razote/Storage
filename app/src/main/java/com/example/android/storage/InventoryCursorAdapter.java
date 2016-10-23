package com.example.android.storage;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.storage.data.StorageContract;

import java.text.NumberFormat;

import static com.example.android.storage.R.id.quantity;

public class InventoryCursorAdapter extends CursorAdapter{

    private final Context mContext;

    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_text_view);

        int idColumnIndex = cursor.getColumnIndex(StorageContract.InventoryEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(StorageContract.InventoryEntry.COLUMN_INVETORY_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(StorageContract.InventoryEntry.COLUMN_INVETORY_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(StorageContract.InventoryEntry.COLUMN_INVETORY_PRICE);

        final int inventoryId = cursor.getInt(idColumnIndex);
        String inventoryName = cursor.getString(nameColumnIndex);
        final int inventoryQuantity = cursor.getInt(quantityColumnIndex);
        int inventoryPrice = cursor.getInt(priceColumnIndex);

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(inventoryPrice);

        nameTextView.setText(inventoryName);
        quantityTextView.setText("Quantity: " + Integer.toString(inventoryQuantity));
        priceTextView.setText(moneyString);

        Button button = (Button) view.findViewById(R.id.click);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int inventoryOnClick = inventoryQuantity;

                if(inventoryOnClick > 0){
                    inventoryOnClick--;
                }
                Uri currentInventoryUri = ContentUris.withAppendedId(StorageContract.InventoryEntry.CONTENT_URI, inventoryId);

                ContentValues values = new ContentValues();
                values.put(StorageContract.InventoryEntry.COLUMN_INVETORY_QUANTITY, inventoryOnClick);

                int rowsAffected = mContext.getContentResolver().update(currentInventoryUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(mContext, R.string.editor_update_inventory_failed,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, R.string.editor_update_inventory_successful,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
