package com.example.familymap.Utilities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Convert {

    public static <T> void toInputStream(T object, OutputStream os) throws IOException
    {
        Gson gson = new Gson();
        writeString(gson.toJson(object), os);
    }

    public static <T> T fromInputStream(Class<T> classOfT, InputStream is) throws IOException
    {
        String json = readString(is);
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

//    public static <T> ArrayList<T> fromInputStream(ArrayList<T> list, InputStream is) throws IOException
//    {
//        Type t = new TypeToken<ArrayList<T>>(){}.getType();
//        String json = readString(is);
//        Gson gson = new Gson();
//        return gson.fromJson(T[].class, t);
//    }

    public static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
