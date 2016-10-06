package com.example.android.storage.data;

import android.provider.BaseColumns;

public final class StorageContract {

    public static final class InventoryEntry implements BaseColumns {

        public final static String TABLE_NAME = "inventory";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_INVETORY_NAME = "name";
        public final static String COLUMN_INVETORY_QUANTITY = "quantity";
        public final static String COLUMN_INVETORY_PRICE = "price";
        public final static String COLUMN_INVETORY_IMG_DIR = "img_dir";
        public final static String COLUMN_INVETORY_SELLABLE = "sellable";

        public static final int SELLABLE_TRUE = 1;
        public static final int SELLABLE_FALSE = 0;

    }

}
