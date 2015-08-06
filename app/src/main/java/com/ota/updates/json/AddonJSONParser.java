package com.ota.updates.json;

import com.ota.updates.db.access.AddonAccess;
import com.ota.updates.items.AddonItem;

import org.json.JSONObject;

/**
 * Created by kryten2k35 on 06/08/15.
 */
public class AddonJSONParser extends BaseJSONParser<AddonItem, AddonAccess> {

    public static String TAG = "AddonJSONParser";

    public AddonJSONParser(String data) {
        super(data, TAG);
    }

    @Override
    public void parseItem(JSONObject jsonObject) {

    }

    @Override
    public void addToDatabase(AddonAccess accessObject) {
    }
}
