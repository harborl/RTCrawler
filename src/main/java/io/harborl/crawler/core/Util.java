package io.harborl.crawler.core;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;


final public class Util {
  
  private Util() { }
  
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  
  public static <T, V> Result<T, V> newResult(final List<T> rets, final V meta) {
    return new Result<T, V>() {

      @Override public int size() {
        return rets.size();
      }

      @Override public List<T> get() {
        return rets;
      }

      @Override public List<T> get(Filter<T> filter) {
        List<T> targets = new ArrayList<T>();
        for (T p : rets) {
          if (filter.test(p)) {
            targets.add(p);
          }
        }
        return targets;
      }

      @Override public V ext() {
        return meta;
      }
    };
  }
  
  /** Ignores the certification validating of all HTTPS requests. */
  public static void setupInsecureSSLContext() {
    try {
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });

      SSLContext context = SSLContext.getInstance("TLS");
      context.init(
        null, 
        new X509TrustManager[] { 
          new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
            public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
          } 
        },
        null
      );

      HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
