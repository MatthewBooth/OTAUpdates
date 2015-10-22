package com.ota.updates.items;
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

public class VersionItem extends BaseItem {
    private String fullName;
    private String slug;
    private String androidVersion;
    private String changelog;
    private String createdAt;
    private String publishedAt;
    private int versionNumber;
    private int deltaUploadId;
    private int fullUploadId;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getDeltaUploadId() {
        return deltaUploadId;
    }

    public void setDeltaUploadId(int deltaUploadId) {
        this.deltaUploadId = deltaUploadId;
    }

    public int getFullUploadId() {
        return fullUploadId;
    }

    public void setFullUploadId(int fullUploadId) {
        this.fullUploadId = fullUploadId;
    }
}
