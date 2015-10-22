package com.ota.updates.utils;

public interface Constants {

    // Debugging
    boolean DEBUGGING = true;

    // Database Version
    int DATABASE_VERSION = 1;
    String DATABASE_NAME = "OtaUpdates.db";

    String NAME_ID = "id";
    String NAME_FULL_NAME = "full_name";
    String NAME_SLUG = "slug";
    String NAME_ANDROID_VERSION = "android_version";
    String NAME_CHANGELOG = "changelog";
    String NAME_UPDATED_AT = "updated_at";
    String NAME_CREATED_AT = "created_at";
    String NAME_PUBLISHED_AT = "published_at";
    String NAME_DOWNLOADS = "downloads";
    String NAME_VERSION_NUMBER = "version_number";
    String NAME_DELTA_ID = "delta_id";
    String NAME_FULL_ID = "full_id";
    String NAME_SIZE = "size";
    String NAME_MD5 = "md5";
    String NAME_STATUS = "status";
    String NAME_NAME = "name";
    String NAME_DESCRIPTION = "description";
    String NAME_DOWNLOAD_LINK = "download_link";
    String ADDON_TABLE_NAME = "addon";
    String UPLOAD_TABLE_NAME = "upload";
    String VERSION_TABLE_NAME = "version";
    String NAME_ROM = "rom";
    String NAME_VERSIONS = "versions";
    String NAME_ADDONS = "addons";
    String NAME_DELTA_UPLOAD = "delta_upload";
    String NAME_FULL_UPLOAD = "full_upload";
    String NAME_VERSION = "version";
    String NAME_ADDON = "addon";
    String NAME_CATEGORY = "category";

    // Font Awesome ttf
    String FONT_AWESOME = "fontawesome-webfont-4.4.0.ttf";

    // Build properties
    String PROP_MANIFEST = "ro.ota.manifest";
    String PROP_VERSION = "ro.ota.version";
    String PROP_DEFAULT_THEME = "ro.ota.default_theme";

    // Preferences
    String LAST_CHECKED_FOR_UPDATE = "last_checked_for_update";
}
