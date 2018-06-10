package ru.shirykalov.anatoly.classiconline.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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
        return readAll(inputStream);
    }

    @Override
    public String postJsonString(URI path, String payload) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) path.toURL().openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty( "Content-type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        writer.write(payload);
        writer.flush();
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));
        return readAll(reader);
    }

    private String readAll(BufferedReader reader) throws IOException {
        String line =reader.readLine();
        String ret = "";
        while(line != null && line.length() != 0 ) {
            ret += line;
            line = reader.readLine();
        }
        return ret;
    }
}
