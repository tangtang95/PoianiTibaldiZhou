package com.trackme.trackmeapplication.httpConnection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public class ConnectionBuilder {

    private ConnectionThread connectionThread;

    public ConnectionBuilder(LockInterface lockInterface) {
        SSL ssl = SSL.getInstance();
        connectionThread = new ConnectionThread(ssl.getSllContext(), ssl.getHostnameVerifier(), lockInterface);
    }

    public ConnectionBuilder setUrl(String url) {
        connectionThread.setUrl(url);
        return this;
    }

    public ConnectionBuilder setHttpMethod(HttpMethod method) {
        connectionThread.setHttpAction(method);
        return this;
    }

    public ConnectionBuilder setEntity(HttpEntity entity) {
        connectionThread.setEntity(entity);
        return this;
    }

    public ConnectionThread getConnection() {
        return connectionThread;
    }

}
