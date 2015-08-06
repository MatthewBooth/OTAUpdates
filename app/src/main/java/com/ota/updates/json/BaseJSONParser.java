package com.ota.updates.json;

public abstract class BaseJSONParser {

    public static String COLUMN_NAME_ROM = "rom";
    public static String COLUMN_NAME_VERSIONS = "versions";
    public static String COLUMN_NAME_ADDONS = "addons";
    public static String COLUMN_NAME_DELTA_UPLOAD = "delta_upload";
    public static String COLUMN_NAME_FULL_UPLOAD = "full_upload";


    public BaseJSONParser() {

    }

    public abstract void parse();

    public abstract void addToDatabase();
}
