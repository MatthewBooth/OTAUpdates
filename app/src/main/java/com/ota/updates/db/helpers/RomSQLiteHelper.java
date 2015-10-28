package com.ota.updates.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.ota.updates.items.RomItem;

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
public class RomSQLiteHelper extends BaseSQLiteHelper {

    public RomSQLiteHelper(Context context) {
        super(context);
    }

    /**
     * Adds an RomItem to the database
     *
     * @param item the RomItem to be added
     */
    public void addRom(RomItem item) {
        ContentValues values = new ContentValues();
        values.put(NAME_ID, item.getId());
        values.put(NAME_NAME, item.getName());
        values.put(NAME_SLUG, item.getSlug());
        values.put(NAME_DESCRIPTION, item.getDescription());
        values.put(NAME_PUBLISHED_AT, item.getPublishedAt());
        values.put(NAME_CREATED_AT, item.getCreatedAt());
        values.put(NAME_DOWNLOADS, item.getDownloads());
        values.put(NAME_WEBSITE_URL, item.getWebsiteUrl());
        values.put(NAME_DONATE_URL, item.getDonateUrl());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insertWithOnConflict(ROM_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    /**
     * Get a single RomItem from the database
     *
     * @return the selected RomItem
     */
    public RomItem getRom() {
        String query = "SELECT * FROM " + ROM_TABLE_NAME + " LIMIT 1";
        return getRomItem(query);
    }

    /**
     * Runs a query on the Upload table in the database
     *
     * @param query The query to be executed
     * @return The resulting UploadItem
     */
    @Nullable
    private RomItem getRomItem(String query) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        RomItem romItem = new RomItem();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            romItem.setId(Integer.parseInt(cursor.getString(0)));
            romItem.setName(cursor.getString(1));
            romItem.setSlug(cursor.getString(2));
            romItem.setDescription(cursor.getString(3));
            romItem.setPublishedAt(cursor.getString(4));
            romItem.setCreatedAt(cursor.getString(5));
            romItem.setDownloads(Integer.parseInt(cursor.getString(6)));
            romItem.setWebsiteUrl(cursor.getString(7));
            romItem.setDonateUrl(cursor.getString(8));
            cursor.close();
        } else {
            romItem = null;
        }
        db.close();
        return romItem;
    }
}
