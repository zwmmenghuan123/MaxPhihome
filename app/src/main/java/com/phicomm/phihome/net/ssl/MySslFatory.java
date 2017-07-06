package com.phicomm.phihome.net.ssl;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * SSL工厂类，暂时设定为信任所有证书
 * <p>
 * Created by qisheng.lv on 2017/4/27.
 */
public class MySslFatory {

    public static SSLSocketFactory createSSlSocketFactory() {
        return createSSlSocketFactory(null);
    }

    public static SSLSocketFactory createSSlSocketFactory(X509TrustManager trustManager) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            if (trustManager == null) {
                trustManager = new TrusAllManager();
            }
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
        }
        return sslSocketFactory;
    }

    public static X509TrustManager createX509TrusManaer() {
        return new TrusAllManager();
    }

    public static HostnameVerifier getHostnameVerifier() {
        return new TrustAllHostnameVerifier();
    }

    private static class TrusAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public static class TrustAllHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
