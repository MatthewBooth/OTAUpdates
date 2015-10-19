package com.ota.updates.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

public class Utils {
    public final static String TAG = "Utils";

    private static final int KILOBYTE = 1024;
    private static int KB = KILOBYTE;
    private static int MB = KB * KB;
    private static int GB = MB * KB;

    private static DecimalFormat decimalFormat = new DecimalFormat("##0.#");

    static {
        decimalFormat.setMaximumIntegerDigits(3);
        decimalFormat.setMaximumFractionDigits(1);
    }

    /**
     * Returns the contents of a file as a String
     *
     * @param file The file to parse into a String
     * @return the returned String
     * @throws IOException An input output exception, usually if the file cannot be found
     */
    public static String getFileContents(final File file) throws IOException {
        InputStreamReader inputReader = null;
        String text;

        StringBuilder data = new StringBuilder();
        char tmp[] = new char[2048];
        int numRead;
        inputReader = new FileReader(file);
        while ((numRead = inputReader.read(tmp)) >= 0) {
            data.append(tmp, 0, numRead);
        }
        text = data.toString();

        if (inputReader != null) {
            inputReader.close();
        }
        return text;
    }

    /**
     * Convert from bytes to megabytes
     * @param size  The size in bytes
     * @return The size in megabytes
     */
    public static String formatDataFromBytes(long size) {

        String symbol;
        KB = KILOBYTE;
        symbol = "B";
        if (size < KB) {
            return decimalFormat.format(size) + symbol;
        } else if (size < MB) {
            return decimalFormat.format(size / (float) KB) + 'k' + symbol;
        } else if (size < GB) {
            return decimalFormat.format(size / (float) MB) + 'M' + symbol;
        }
        return decimalFormat.format(size / (float) GB) + 'G' + symbol;
    }

    /**
     * Returns the hostname of a URL
     * Will strip out "www." if it is found
     * @param url  The url we wish to use
     * @return a String with just the hostname
     */
    public static String getUrlHost(String url) {
        URI uri;
        String result = null;
        try {
            uri = new URI(url);
            String domain = uri.getHost();
            result = domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Downloads a file to the private storage for the app in /data/data/com.ota.updates/files
     *
     * @param context
     * @param fileUrl  The URL of the file to download, not including the filename
     * @param fileName The name of the file to download
     * @throws IOException
     */
    public static void downloadFile(Context context, String fileUrl, String fileName) throws IOException {
        // An InputStream object
        InputStream input;

        // A new connection to a remote file
        URL url = new URL(fileUrl + fileName);
        URLConnection connection = url.openConnection();
        connection.connect();

        // Downloading the file
        input = new BufferedInputStream(url.openStream());
        OutputStream output = context.openFileOutput(fileName, Context.MODE_PRIVATE);

        byte data[] = new byte[KILOBYTE];
        int count;
        while ((count = input.read(data)) != -1) {
            output.write(data, 0, count);
        }

        // Flush the buffers and close the streams
        output.flush();
        output.close();
        input.close();
    }

    /**
     * Opens a given URL
     * @param context  The context that will open the URL
     * @param url  The URL to open
     */
    public static void openWebsite(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}