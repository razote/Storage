package com.example.android.storage.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import static android.text.style.TtsSpan.GENDER_FEMALE;
import static android.text.style.TtsSpan.GENDER_MALE;

public final class StorageContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.storage";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";

    public static final class InventoryEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public final static String TABLE_NAME = "inventory";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_INVETORY_NAME = "name";
        public final static String COLUMN_INVETORY_QUANTITY = "quantity";
        public final static String COLUMN_INVETORY_PRICE = "price";
        public final static String COLUMN_INVETORY_IMG_DIR = "img_dir";
        public final static String COLUMN_INVETORY_SELLABLE = "sellable";

        public static final int SELLABLE_TRUE = 1;
        public static final int SELLABLE_FALSE = 0;

        public static boolean isValidSellable(int sell) {
            if (sell == SELLABLE_TRUE || sell == SELLABLE_FALSE) {
                return true;
            }
                return false;
            }
    }

}
