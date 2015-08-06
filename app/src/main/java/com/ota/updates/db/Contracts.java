package com.ota.updates.db;

import android.provider.BaseColumns;

public class Contracts {


    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_FULL_NAME = "full_name";
    public static final String COLUMN_NAME_SLUG = "slug";
    public static final String COLUMN_NAME_ANDROID_VERSION = "android_version";
    public static final String COLUMN_NAME_CHANGELOG = "changelog";
    public static final String COLUMN_NAME_UPDATED_AT = "updated_at";
    public static final String COLUMN_NAME_CREATED_AT = "created_At";
    public static final String COLUMN_NAME_PUBLISHED_AT = "published_at";
    public static final String COLUMN_NAME_DOWNLOADS = "downloads";
    public static final String COLUMN_NAME_VERSION_NUMBER = "version_number";
    public static final String COLUMN_NAME_DELTA_ID = "delta_id";
    public static final String COLUMN_NAME_FULL_ID = "full_id";
    public static final String COLUMN_NAME_SIZE = "size";
    public static final String COLUMN_NAME_MD5 = "md5";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_DOWNLOAD_LINK = "download_link";

    public Contracts() {
    }

    public static abstract class VersionContract implements BaseColumns {
        public static final String TABLE_NAME = "version";
    }

    public static abstract class UploadContract implements BaseColumns {
        public static final String TABLE_NAME = "upload";
    }

    public static abstract class AddonContract implements BaseColumns {
        public static final String TABLE_NAME = "addon";
    }
}
