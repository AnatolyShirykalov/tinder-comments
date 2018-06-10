package ru.shirykalov.anatoly.classiconline;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class CommentUtils {

    public static List<Comment> parseComments(String rawJSON) {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JSONObject object = new JSONObject(rawJSON);
            List<Comment> commentList = new ArrayList<>();
            JSONArray data = object.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                Comment comment = gson.fromJson(data.getString(i), Comment.class);
                commentList.add(comment);
            }

            System.err.println(commentList.get(0).toString());
            return commentList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getJsonFromServer(String url) throws IOException {

        BufferedReader inputStream = null;

        URL jsonUrl = new URL(url);
        URLConnection dc = jsonUrl.openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        inputStream = new BufferedReader(new InputStreamReader(
                dc.getInputStream()));

        // read the JSON results into a string
        String jsonResult = inputStream.readLine();
        return jsonResult;
    }
}
