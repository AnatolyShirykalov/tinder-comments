package ru.shirykalov.anatoly.classiconline;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.net.URI;

import ru.shirykalov.anatoly.classiconline.remote.HttpRemoteManager;

public class PostDataTask extends AsyncTask<String, Void, String> {
    private WeakReference<Context> contextRef;

    public PostDataTask(Context context) {
        this.contextRef = new WeakReference<>(context);
    }
    @Override
    protected String doInBackground(String... strings) {
        if(strings.length == 0) return null;
        Context context = contextRef.get();
        if (context == null) return null;
        try {
            new HttpRemoteManager()
                    .postJsonString(
                            URI.create(context.getString(R.string.remote)),
                            strings[0]
                            );
        } catch (Exception e) {
            Log.e("PostDataError",e.getMessage());
        }
        return null;
    }
}
