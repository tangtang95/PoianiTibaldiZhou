package com.trackme.trackmeapplication.httpConnection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public class ConnectionBuilder {

    private ConnectionAsyncTask connectionAsyncTask = ConnectionAsyncTask.getInstance();

    public ConnectionBuilder() {
        connectionAsyncTask.setIsLockTrue();
    }

    public ConnectionBuilder setUrl(String url) {
        connectionAsyncTask.setUrl(url);
        return this;
    }

    public ConnectionBuilder setHttpMethod(HttpMethod method) {
        connectionAsyncTask.setHttpAction(method);
        return this;
    }

    public ConnectionBuilder setEntity(HttpEntity entity) {
        connectionAsyncTask.setEntity(entity);
        return this;
    }

    public ConnectionAsyncTask getConnection() {
        return connectionAsyncTask;
    }

}
