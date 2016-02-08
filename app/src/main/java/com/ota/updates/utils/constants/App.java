package com.ota.updates.utils.constants;
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

import com.ota.updates.utils.Utils;

public interface App {

    // Debugging
    Boolean DEBUGGING = true;

    // Database Version
    Integer DATABASE_VERSION = 4;
    String DATABASE_NAME = "OtaUpdates.db";

    // Fonts
    String FONT_AWESOME = "fontawesome-webfont-4.5.0.ttf";
    String MATERIAL_ICONS = "material-icons-2.1.3.ttf";

    // Build properties
    String PROP_MANIFEST = "ro.ota.manifest";
    String PROP_VERSION = "ro.ota.version";
    String PROP_DEFAULT_THEME = "ro.ota.default_theme";
    String PROP_DOWNLOAD_LOC = "ro.ota.download_loc";

    // File Types
    Integer FILE_TYPE_VERSION = 0;
    Integer FILE_TYPE_ADDON = 1;

    // Download directory
    String OTA_DOWNLOAD_DIR = Utils.doesPropExist(PROP_DOWNLOAD_LOC)
            ? Utils.getProp(PROP_DOWNLOAD_LOC)
            : "OTAUpdates";

    // Permissions
    Integer PERMISSIONS_REQUEST_STORAGE = 0;

    // Download Types
    Integer DOWNLOAD_TYPE_VERSION = 0;
    Integer DOWNLOAD_TYPE_ADDON = 1;

    // Download Status
    Integer DOWNLOAD_STATUS_RUNNING = 0;
    Integer DOWNLOAD_STATUS_FINISHED = 1;
    Integer DOWNLOAD_STATUS_FAILED = 2;
}
