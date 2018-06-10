package ru.shirykalov.anatoly.classiconline;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import com.mindorks.placeholderview.SwipePlaceHolderView;

import junit.framework.Assert;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.List;

import ru.shirykalov.anatoly.classiconline.remote.RemoteManager;

public class LoadDataTask extends AsyncTask<URI, Void, String> {

    private WeakReference<Context> contextRef;
    private WeakReference<SwipePlaceHolderView> viewRef;
    private WeakReference<RemoteManager> remoteManager;
    private WeakReference<TextView> textRef;

    private volatile String errorMessage;

    public LoadDataTask(Context context, SwipePlaceHolderView view, TextView text, RemoteManager remoteManager) {
        this.contextRef = new WeakReference<>(context);
        this.viewRef = new WeakReference<>(view);
        this.remoteManager = new WeakReference<>(remoteManager);
        this.textRef = new WeakReference<>(text);
    }

    @Override
    protected String doInBackground(URI... uris) {
        try {
            RemoteManager manager = remoteManager.get();
            if (manager == null) {
                throw new AbsentRemoteManagerException("Remote manager is null, cannot load data from remote");
            }
            return manager.getJsonString(uris[0]);
        } catch (IOException | AbsentRemoteManagerException e) {
            errorMessage = e.getMessage();
            this.cancel(true);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        SwipePlaceHolderView view = viewRef.get();
        Context context = contextRef.get();
        if (view == null || context == null) return;
        view.removeAllViews();
        List<Comment> comments = CommentUtils.parseComments(s);

        Assert.assertNotNull("Comments shouldn't be null", comments);

        for (Comment comment : comments) {
            view.addView(new TinderCommentCard(context, comment, view));
        }

    }

    @Override
    protected void onCancelled(String s) {
        TextView text = textRef.get();
        if (text == null) return;
        text.setText(this.errorMessage);
    }

    public WeakReference<Context> getContextRef() {
        return contextRef;
    }

    public WeakReference<SwipePlaceHolderView> getViewRef() {
        return viewRef;
    }
}
