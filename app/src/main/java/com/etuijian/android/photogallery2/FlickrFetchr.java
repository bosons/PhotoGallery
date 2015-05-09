package com.etuijian.android.photogallery2;

import android.net.Uri;
import android.util.Log;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;


/**
 * Created by xyang on 5/8/15.
 */
public class FlickrFetchr {
    private static final String API_KEY = "b0a574d9e231935b3dd06f6186a10ab7";
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new HttpResponseException(connection.getResponseCode(),
                        "A bad response occurred: " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    public List<GalleryItem> fetchItem() {
        List<GalleryItem> items = new ArrayList<GalleryItem>();
        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = getUrl(url);

            Log.i("Eric", "Received Json: " + jsonString);
            Map<String, Object> gsonBody = new Gson().fromJson(jsonString, Map.class);
            parseItems(items, gsonBody);
        } catch (IOException ioe) {
            Log.e("Eric", "failed to fetch items: ", ioe);
        } catch (JSONException je) {
            Log.e("Eric", "failed to parse json string");
        }
        return items;
    }

    private void parseItems(List<GalleryItem> items, Map<String, Object> gsonBody)
            throws IOException, JSONException{
        List<Object> photoList =
                (ArrayList)(
                  ((Map) gsonBody.get("photos")).get("photo"));

        for(int i =0; i < photoList.size(); i++){
            Map<String,String> map = (Map<String,String>)photoList.get(i);
            GalleryItem item = new GalleryItem();
            item.setId(map.get("id"));;
            item.setCaption(map.get("title"));
            item.setUrl(map.get("url_s"));
            items.add(item);
        }
    }
}
