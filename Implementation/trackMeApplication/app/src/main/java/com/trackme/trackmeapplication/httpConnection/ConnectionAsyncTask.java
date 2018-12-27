package com.trackme.trackmeapplication.httpConnection;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class ConnectionAsyncTask extends AsyncTask<String, Void, String> {

    private static ConnectionAsyncTask instance = null;

    private URL url;
    private HttpMethod httpAction;
    private String entity;

    private HttpStatus statusReturned;
    private String stringReturned;

    private static final String KEY_STORE_TYPE = "PKCS12";
    private static final char[] password = {'b','i','c','m','o','u','s','e','p','e','n','c','i','l'};

    private SSLContext sllContext;
    private HostnameVerifier hostnameVerifier;

    private final Object lock = new Object();
    private boolean isLock;

    private ConnectionAsyncTask() {}

    void setIsLockTrue() {
        isLock = true;
    }

    void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    void setHttpAction(HttpMethod httpAction) {
        this.httpAction = httpAction;
    }

    void setEntity(HttpEntity entity) {
        this.entity = entity.toString();
    }

    public static ConnectionAsyncTask getInstance(){
        if(instance == null)
            instance = new ConnectionAsyncTask();
        return instance;
    }

    public void setUpSLLConnection(InputStream keyStoreIn) {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            keyStore.load(keyStoreIn, password);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            sllContext = SSLContext.getInstance("TLS");
            sllContext.init(null, tmf.getTrustManagers(), null);

            hostnameVerifier = (hostname, session) -> hostname.equals(Settings.getServerAddress());

        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException |
                KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        synchronized (lock) {
            isLock = true;
            HttpsURLConnection urlConnection;
            try {
                urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setHostnameVerifier(hostnameVerifier);
                urlConnection.setSSLSocketFactory(sllContext.getSocketFactory());

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                writeStream(out);

                Log.d("STATUS:", String.valueOf(urlConnection.getResponseCode()));
                if (urlConnection.getResponseCode() != HttpStatus.OK.value())
                    return String.valueOf(urlConnection.getResponseCode());

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                return readStream(in);
            } catch (IOException e) {
                return null;
            }
        }
    }

    private void writeStream(OutputStream out) throws IOException {
        out.write(entity.getBytes());
        out.flush();
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder message = new StringBuilder();

        for (String line; (line = r.readLine()) != null; ) {
            message.append(line).append('\n');
        }

        return message.toString();
    }

    @Override
    protected void onPostExecute(String stringResponseEntity) {
        this.stringReturned = stringResponseEntity;
        this.statusReturned = HttpStatus.OK;
        isLock = false;
        lock.notifyAll();
    }

    public void onResponce() {

    }

    public String getResponse() {
        synchronized (lock) {
            while (isLock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    //
                }
            }
            return stringReturned;
        }
    }

    public HttpStatus getStatusReturned() {
        synchronized (lock) {
            while (isLock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    //
                }
            }
            return statusReturned;
        }
    }
}
