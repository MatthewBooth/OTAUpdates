package com.ota.updates.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
}
