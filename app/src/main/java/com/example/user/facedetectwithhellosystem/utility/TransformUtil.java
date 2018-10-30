package com.example.user.facedetectwithhellosystem.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by jenny on 2017/8/24.
 */

public class TransformUtil {

    public String getEncodedData(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (String key : data.keySet()) {
            System.out.println("key - " + key);
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
                System.out.println("value - " + value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println("UnsupportedEncodingException - " + e.toString());
            }

            if (sb.length() > 0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

    public Bitmap base64ToBitmap(String _data) {
        byte[] imageAsBytes = Base64.decode(_data.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public String bitmapToBase64(Bitmap _bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        _bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }


}
