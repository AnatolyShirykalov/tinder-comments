package ru.shirykalov.anatoly.classiconline.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;

public class HttpRemoteManager implements RemoteManager {

    public HttpRemoteManager() {
    }

    @Override
    public String getJsonString(URI path) throws IOException {

        BufferedReader inputStream = null;

        URLConnection dc = path.toURL().openConnection();

        dc.setConnectTimeout(5000);
        dc.setReadTimeout(5000);

        InputStreamReader in = new InputStreamReader(
                dc.getInputStream());
        inputStream = new BufferedReader(in);

        // read the JSON results into a string
        String jsonResult = inputStream.readLine();
        return jsonResult;
    }
}
