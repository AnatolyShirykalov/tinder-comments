package ru.shirykalov.anatoly.classiconline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import junit.framework.Assert;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.List;

import ru.shirykalov.anatoly.classiconline.remote.RemoteManager;

public class LoadDataTask extends AsyncTask<URI, Void, String> {

    private WeakReference<Context> contextRef;
    private WeakReference<RemoteManager> remoteManager;
    private WeakReference<LinearLayout> mainViewRef;

    private volatile String errorMessage;

    public LoadDataTask(Context context, RemoteManager remoteManager, LinearLayout mainView) {
        this.contextRef = new WeakReference<>(context);
        this.remoteManager = new WeakReference<>(remoteManager);
        this.mainViewRef = new WeakReference<>(mainView);
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
        Context context = contextRef.get();
        LinearLayout mainView = mainViewRef.get();
        if (mainView == null || context == null) return;
        mainView.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.tinder_view, null);
        SwipePlaceHolderView view = (SwipePlaceHolderView) inflate;
        view.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        List<Comment> comments = CommentUtils.parseComments(s);

        Assert.assertNotNull("Comments shouldn't be null", comments);

        for (Comment comment : comments) {
            try {
                view.addView(new TinderCommentCard(context, comment, view));
            } catch (Exception e) {
                System.err.println("Comment: " + comment + "; View: " + view);
            }
        }

        mainView.addView(view);
    }

    @Override
    protected void onCancelled(String s) {
        Context context = contextRef.get();
        LinearLayout mainView = mainViewRef.get();

        if (context == null || mainView == null) return;
        mainView.removeAllViews();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.error_view, null);
        mainView.addView(view);

        ((TextView) view).setText(errorMessage);
    }

    public WeakReference<Context> getContextRef() {
        return contextRef;
    }
}
