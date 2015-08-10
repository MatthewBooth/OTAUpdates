package com.ota.updates;

public class Addon {
	
	private String mTitle;
	private String mDesc;
	private String mPublishedAt;
	private int mFilesize;
	private String mDownloadLink;
	private int mId;
	
	public void setTitle(String input) {
		mTitle = input;		
	}
	
	public void setDesc(String input) {
		mDesc = input;		
	}

	public void setPublishedAt(String input) {
		mPublishedAt = input;		
	}
	
	public void setFilesize(int input) {
		mFilesize = input;		
	}
	
	public void setDownloadLink(String input) {
		mDownloadLink = input;		
	}

	public void setId(int input) {
		mId = input;		
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String getDesc() {
		return mDesc;
	}
	
	public String getPublishedAt() {
		return mPublishedAt;
	}
	
	public String getDownloadLink() {
		return mDownloadLink;
	}
	
	public int getFilesize() {
		return mFilesize;
	}
	
	public int getId() {
		return mId;
	}
}
