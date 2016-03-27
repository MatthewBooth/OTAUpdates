package com.ota.updates.db.helpers;
/*
 * Copyright (C) 2015 Matt Booth.
 *
 * Licensed under the Attribution-NonCommercial-ShareAlike 4.0 International
 * (the "License") you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ota.updates.items.AddonItem;

import java.util.ArrayList;

public class AddonSQLiteHelper extends BaseSQLiteHelper {

    private static AddonSQLiteHelper mInstance = null;

    public static synchronized AddonSQLiteHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new AddonSQLiteHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public AddonSQLiteHelper(Context context) {
        super(context);
    }

    /**
     * Adds an AddonItem to the database
     * @param item  the AddonItem to be added
     */
    public synchronized void addAddon(AddonItem item) {
        ContentValues values = new ContentValues();
        values.put(NAME_ID, item.getId());
        values.put(NAME_DOWNLOADS, item.getDownloads());
        values.put(NAME_NAME, item.getName());
        values.put(NAME_SLUG, item.getSlug());
        values.put(NAME_DESCRIPTION, item.getDescription());
        values.put(NAME_CREATED_AT, item.getCreatedAt());
        values.put(NAME_PUBLISHED_AT, item.getPublishedAt());
        values.put(NAME_SIZE, item.getSize());
        values.put(NAME_MD5, item.getMd5());
        values.put(NAME_DOWNLOAD_LINK, item.getDownloadLink());
        values.put(NAME_CATEGORY, item.getCategory());

        SQLiteDatabase db = getWritableDb();
        db.beginTransaction();
        Long result = db.insertWithOnConflict(ADDON_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (result != -1) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    /**
     * Get a single AddonItem from the database
     * @param id  the ID of the item to be retrieved
     * @return the selected AddonItem
     */
    public synchronized AddonItem getAddon(int id) {
        String query = "SELECT * FROM " + ADDON_TABLE_NAME + " WHERE " + NAME_ID + " =  \"" + id + "\"";
        return getAddonItem(query);
    }

    /**
     * Gets the lastest AddonItem in the database
     * @return the AddonItem that was requested
     */
    public synchronized AddonItem getLastAddon() {
        String query = "SELECT * FROM " + ADDON_TABLE_NAME + " ORDER BY " + NAME_ID + " DESC LIMIT 1";
        return getAddonItem(query);
    }

    /**
     * Runs a query on the Addon table in the database
     * @param query  The query to be executed
     * @return The resulting AddonItem
     */
    @Nullable
    private synchronized AddonItem getAddonItem(String query) {
        SQLiteDatabase db = getReadableDb();

        db.beginTransaction();
        Cursor cursor = db.rawQuery(query, null);

        AddonItem addonItem;

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            addonItem = getAddonItemFromCursor(cursor);
            cursor.close();
            db.setTransactionSuccessful();
        } else {
            addonItem = null;
        }
        db.endTransaction();
        return addonItem;
    }

    public synchronized ArrayList<AddonItem> getListOfAddons() {
        ArrayList<AddonItem> list = new ArrayList<>();

        Cursor cursor = getAllEntries(ADDON_TABLE_NAME);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                AddonItem addonItem = getAddonItemFromCursor(cursor);
                list.add(addonItem);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    @NonNull
    private synchronized AddonItem getAddonItemFromCursor(Cursor cursor) {
        AddonItem addonItem = new AddonItem();
        addonItem.setId(Integer.parseInt(cursor.getString(0)));
        addonItem.setName(cursor.getString(1));
        addonItem.setSlug(cursor.getString(2));
        addonItem.setDescription(cursor.getString(3));
        addonItem.setCreatedAt(cursor.getString(4));
        addonItem.setPublishedAt(cursor.getString(5));
        addonItem.setDownloads(Integer.parseInt(cursor.getString(6)));
        addonItem.setSize(Integer.parseInt(cursor.getString(7)));
        addonItem.setMd5(cursor.getString(8));
        addonItem.setDownloadLink(cursor.getString(9));
        addonItem.setCategory(cursor.getString(10));
        return addonItem;
    }
}
