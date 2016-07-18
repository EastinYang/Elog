package com.eastin.log.util;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Eastin on 16/7/18.
 */
public class FileUtil {
    private static String filePath = Environment.getExternalStorageDirectory() + File.separator;
    private static String fileName = "log.txt";

    private static File getFile() {
        File file = new File(filePath + fileName);
        try {
            if (!file.exists()) {
                File path = new File(filePath);
                if (!path.exists()) {
                    path.mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String writeStringToFile(String data) {
        try {
            File file = getFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data.getBytes());
            outputStream.flush();
            outputStream.close();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
