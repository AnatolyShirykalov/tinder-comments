package ru.shirykalov.anatoly.classiconline;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static ru.shirykalov.anatoly.classiconline.TinderUtils.loadJSONFromAsset;

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

    public static List<Comment> loadComments(Context context) {
        try {

            return parseComments(loadJSONFromAsset(context, "comments.json"));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
