package com.ota.updates.json;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ota.updates.db.access.BaseAccess;
import com.ota.updates.items.BaseItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class BaseJSONParser<T extends BaseItem, D extends BaseAccess> {

    protected JSONObject mJSONObject;
    protected ArrayList<T> mItem;
    protected D mAccessObject;

    public BaseJSONParser(String data, String tag, D accessObject) {
        mAccessObject = accessObject;
        try {
            mJSONObject = new JSONObject(data);
        } catch (JSONException e) {
            Log.d(tag, e.getMessage());
        }
    }

    public String toString() {
        return mJSONObject.toString();
    }

    public ArrayList<T> getItemList() {
        return mItem;
    }

    public abstract void parseItem(JSONObject jsonObject);

    public abstract void addToDatabase(D accessObject);
}
