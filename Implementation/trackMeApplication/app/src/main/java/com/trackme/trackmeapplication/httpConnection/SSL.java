package com.trackme.trackmeapplication.httpConnection;

import com.trackme.trackmeapplication.baseUtility.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SSL {

    private static SSL instance = null;

    private SSLContext sllContext;
    private HostnameVerifier hostnameVerifier;

    private SSL(){}

    public static SSL getInstance(){
        if(instance == null)
            instance = new SSL();
        return instance;
    }

    public void setUpSLLConnection(InputStream keyStoreIn) {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(Constant.KEY_STORE_TYPE);
            keyStore.load(keyStoreIn, Constant.password);

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

    SSLContext getSllContext() {
        return sllContext;
    }

    HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }
}
