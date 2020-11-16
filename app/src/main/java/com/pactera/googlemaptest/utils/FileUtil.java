package com.pactera.googlemaptest.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String readJSonFile(Context mContext) {

        try {
            InputStream inputStream = mContext.getAssets().open("lineData.json");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte buf[] = new byte[1024];

            int len;

            while ((len = inputStream.read(buf)) != -1) {

                outputStream.write(buf, 0, len);

            }

            outputStream.close();

            inputStream.close();


            return outputStream.toString();

        } catch (IOException e) {

        }
        return "";

    }


}
