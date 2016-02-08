package com.ota.updates.items;

import java.sql.Timestamp;

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
public class DownloadItem {

    private int mFileId;

    private long mDownloadId;

    private Integer mDownloadType;

    private Timestamp mDownloadStarted;

    private Timestamp mDownloadFinished;

    private Integer mDownloadStatus;

    public DownloadItem() {

    }

    public int getFileId() {
        return mFileId;
    }

    public void setFileId(int fileId) {
        mFileId = fileId;
    }

    public long getDownloadId() {
        return mDownloadId;
    }

    public void setDownloadId(long downloadId) {
        mDownloadId = downloadId;
    }

    public Integer getDownloadType() {
        return mDownloadType;
    }

    public void setDownloadType(Integer mDownloadType) {
        this.mDownloadType = mDownloadType;
    }

    public Timestamp getDownloadStarted() {
        return mDownloadStarted;
    }

    public void setDownloadStarted(Timestamp mDownloadStarted) {
        this.mDownloadStarted = mDownloadStarted;
    }

    public Timestamp getDownloadFinished() {
        return mDownloadFinished;
    }

    public void setDownloadFinished(Timestamp mDownloadFinished) {
        this.mDownloadFinished = mDownloadFinished;
    }

    public Integer getDownloadStatus() {
        return mDownloadStatus;
    }

    public void setDownloadStatus(Integer mDownloadStatus) {
        this.mDownloadStatus = mDownloadStatus;
    }
}
