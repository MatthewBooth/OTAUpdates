package com.ota.updates.rom;

public class Version {
    private int id;
    private String fullName;
    private String slug;
    private String androidVersion;
    private String changelog;
    private String updatedAt;
    private String createdAt;
    private String publishedAt;
    private int downloads;
    private int versionNumber;
    private Upload deltaUpload;
    private Upload fullUpload;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Upload getDeltaUpload() {
        return deltaUpload;
    }

    public void setDeltaUpload(Upload deltaUpload) {
        this.deltaUpload = deltaUpload;
    }

    public Upload getFullUpload() {
        return fullUpload;
    }

    public void setFullUpload(Upload fullUpload) {
        this.fullUpload = fullUpload;
    }
}
