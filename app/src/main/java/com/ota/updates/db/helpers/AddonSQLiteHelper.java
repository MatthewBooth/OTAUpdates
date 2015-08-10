package com.ota.updates.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.ota.updates.items.AddonItem;

public class AddonSQLiteHelper extends BaseSQLiteHelper {

    public AddonSQLiteHelper(Context context) {
        super(context);
    }

    /**
     * Adds an AddonItem to the database
     * @param item  the AddonItem to be added
     */
    public void addAddon(AddonItem item) {
        ContentValues values = new ContentValues();
        values.put(NAME_ID, item.getId());
        values.put(NAME_DOWNLOADS, item.getDownloads());
        values.put(NAME_NAME, item.getName());
        values.put(NAME_SLUG, item.getSlug());
        values.put(NAME_DESCRIPTION, item.getDescription());
        values.put(NAME_UPDATED_AT, item.getUpdatedAt());
        values.put(NAME_CREATED_AT, item.getCreatedAt());
        values.put(NAME_PUBLISHED_AT, item.getPublishedAt());
        values.put(NAME_SIZE, item.getSize());
        values.put(NAME_MD5, item.getMd5());
        values.put(NAME_DOWNLOAD_LINK, item.getDownloadLink());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insertWithOnConflict(ADDON_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Get a single AddonItem from the database
     * @param id  the ID of the item to be retrieved
     * @return the selected AddonItem
     */
    public AddonItem getAddon(int id) {
        String query = "SELECT * FROM " + UPLOAD_TABLE_NAME + " WHERE " + NAME_ID + " =  \"" + id + "\"";
        return getAddonItem(query);
    }

    /**
     * Gets the lastest AddonItem in the database
     * @return the AddonItem that was requested
     */
    public AddonItem getLastAddon() {
        String query = "SELECT * FROM " + UPLOAD_TABLE_NAME + " ORDER BY " + NAME_ID + " DESC LIMIT 1";
        return getAddonItem(query);
    }

    /**
     * Runs a query on the Addon table in the database
     * @param query  The query to be executed
     * @return The resulting AddonItem
     */
    @Nullable
    private AddonItem getAddonItem(String query) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        AddonItem addonItem = new AddonItem();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            addonItem.setId(Integer.parseInt(cursor.getString(0)));
            addonItem.setName(cursor.getString(1));
            addonItem.setSlug(cursor.getString(2));
            addonItem.setDescription(cursor.getString(3));
            addonItem.setUpdatedAt(cursor.getString(4));
            addonItem.setCreatedAt(cursor.getString(5));
            addonItem.setPublishedAt(cursor.getString(6));
            addonItem.setDownloads(Integer.parseInt(cursor.getString(7)));
            addonItem.setSize(Integer.parseInt(cursor.getString(8)));
            addonItem.setMd5(cursor.getString(9));
            addonItem.setDownloadLink(cursor.getString(10));
            cursor.close();
        } else {
            addonItem = null;
        }
        db.close();
        return addonItem;
    }
}
