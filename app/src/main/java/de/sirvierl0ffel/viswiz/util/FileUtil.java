package de.sirvierl0ffel.viswiz.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

// https://stackoverflow.com/a/14377185/13356588

public class FileUtil {

    public static void writeToFile(Context context,
                                   String name,
                                   String data) {
        try {
            File file = new File(context.getFilesDir(), name);
            if (!file.exists() && !file.createNewFile()) {
                Log.e(FileUtil.class.getSimpleName(), "File creation failed!");
                return;
            }
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(name, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(FileUtil.class.getSimpleName(), "File write failed: " + e);
        }
    }

    public static String readFromFile(Context context, String name, String def) {
        String ret = def;

        try {
            InputStream inputStream = context.openFileInput(name);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(FileUtil.class.getSimpleName(), "File not found: " + e);
        } catch (IOException e) {
            Log.e(FileUtil.class.getSimpleName(), "Can not read file: " + e);
        }

        return ret;
    }

}
