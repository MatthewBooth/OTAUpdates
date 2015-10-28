package com.ota.updates.utils;
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

public interface Constants {

    // Debugging
    boolean DEBUGGING = true;

    // Database Version
    int DATABASE_VERSION = 2;
    String DATABASE_NAME = "OtaUpdates.db";

    // Table names
    String ADDON_TABLE_NAME = "addon";
    String UPLOAD_TABLE_NAME = "upload";
    String VERSION_TABLE_NAME = "version";
    String ROM_TABLE_NAME = "rom";

    // Column/Field names
    String NAME_ID = "id";
    String NAME_FULL_NAME = "full_name";
    String NAME_SLUG = "slug";
    String NAME_ANDROID_VERSION = "android_version";
    String NAME_CHANGELOG = "changelog";
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
    String NAME_ROM = "rom";
    String NAME_VERSIONS = "versions";
    String NAME_ADDONS = "addons";
    String NAME_DELTA_UPLOAD = "delta_upload";
    String NAME_FULL_UPLOAD = "full_upload";
    String NAME_VERSION = "version";
    String NAME_ADDON = "addon";
    String NAME_CATEGORY = "category";
    String NAME_WEBSITE_URL = "website_url";
    String NAME_DONATE_URL = "donate_url";

    // Font Awesome ttf
    String FONT_AWESOME = "fontawesome-webfont-4.4.0.ttf";

    // Build properties
    String PROP_MANIFEST = "ro.ota.manifest";
    String PROP_VERSION = "ro.ota.version";
    String PROP_DEFAULT_THEME = "ro.ota.default_theme";

    // Preferences
    String LAST_CHECKED_FOR_UPDATE = "last_checked_for_update";

    // File Types
    Integer FILE_TYPE_VERSION = 0;
    Integer FILE_TYPE_ADDON = 1;
}
