package com.hyeongarl.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidator {

    /**
     * URL 유효성 검사
     * @param urlString 입력한 url 값
     * @return 유효값(true, false)
     */
    public static boolean checkUrl(String urlString) {
        if(isValidURL(urlString)) {
            return isURLAccessible(urlString);
        } else {
            return false;
        }
    }

    public  static boolean isValidURL(String urlString) {
        try{
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean isURLAccessible(String urlString) {
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(500);
            connection.setReadTimeout(500);

            int responseCode = connection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            return false;
        }
    }
}
