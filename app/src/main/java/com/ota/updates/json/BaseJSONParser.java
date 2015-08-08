package com.ota.updates.json;

import android.content.Context;

import com.ota.updates.db.access.BaseAccess;
import com.ota.updates.items.BaseItem;
import com.ota.updates.utils.Constants;

public abstract class BaseJSONParser implements Constants {

    public static String NAME_ROM = "rom";
    public static String NAME_VERSIONS = "versions";
    public static String NAME_ADDONS = "addons";
    public static String NAME_DELTA_UPLOAD = "delta_upload";
    public static String NAME_FULL_UPLOAD = "full_upload";

    protected String mJSONString;
    protected Context mContext;

    /**
     * @param context
     * @param jsonString
     */
    public BaseJSONParser(Context context, String jsonString) {
        mJSONString = jsonString;
        mContext = context;
        parse();
    }

    /**
     *
     * @param item
     * @param access
     * @param <Item>
     * @param <Access>
     */
    public <Item extends BaseItem, Access extends BaseAccess<Item>> void addToDatabase(Item item, Access access) {
        access.open();
        access.put(item);
        access.close();
    }

    /**
     *
     */
    public abstract void parse();
}
