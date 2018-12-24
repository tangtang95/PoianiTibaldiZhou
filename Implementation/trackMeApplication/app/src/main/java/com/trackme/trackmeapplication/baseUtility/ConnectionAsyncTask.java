package com.trackme.trackmeapplication.baseUtility;

import android.os.AsyncTask;

import com.trackme.trackmeapplication.account.network.AccountNetworkImp;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ConnectionAsyncTask extends AsyncTask<String, String, Void> {

    private String url;
    private HttpMethod httpAction;
    private HttpEntity<?> entity;
    private RestTemplate restTemplate;
    private ResponseEntity<String>  response = null;

    private final Object lock;

    public ConnectionAsyncTask(String url, HttpMethod httpAction, HttpEntity entity, Object lock) {
        this.url = url;
        this.httpAction = httpAction;
        this.entity = entity;
        this.restTemplate = new RestTemplate();
        this.lock = lock;
    }

    @Override
    protected Void doInBackground(String... strings) {
        synchronized (lock) {
            response = restTemplate.exchange(url,
                    httpAction, entity, String.class);
            AccountNetworkImp.setIsLock(false);
            lock.notifyAll();
        }
        return null;
    }

    public ResponseEntity<String> getResponse() {
        return response;
    }
}
