package ru.shirykalov.anatoly.classiconline.remote;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;

public interface RemoteManager {

    String getJsonString(URI path) throws IOException;

}
