package ru.shirykalov.anatoly.classiconline;

import android.content.Context;
import android.os.AsyncTask;

import com.mindorks.placeholderview.SwipePlaceHolderView;

import junit.framework.Assert;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import static ru.shirykalov.anatoly.classiconline.CommentUtils.getJsonFromServer;

public class LoadDataTask extends AsyncTask<String, Void, String> {

    private WeakReference<Context> contextRef;
    private WeakReference<SwipePlaceHolderView> viewRef;

    public LoadDataTask(Context context, SwipePlaceHolderView view) {
        this.contextRef = new WeakReference<>(context);
        this.viewRef = new WeakReference<>(view);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            return getJsonFromServer(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        viewRef.get().removeAllViews();
        List<Comment> comments = CommentUtils.parseComments(s);

        Assert.assertNotNull("Comments shouldn't be null", comments);

        for (Comment comment : comments) {
            viewRef.get().addView(new TinderCommentCard(contextRef.get(), comment, viewRef.get()));
        }
    }

    public WeakReference<Context> getContextRef() {
        return contextRef;
    }

    public WeakReference<SwipePlaceHolderView> getViewRef() {
        return viewRef;
    }
}
