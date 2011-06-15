package org.mule.modules.amazon.fws.util;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by IntelliJ IDEA.
 * User: davideason
 * Date: 6/14/11
 * Time: 4:01 PM
 */
public class CustomTrustManager implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        for (X509Certificate x509Certificate : x509Certificates) {
            System.out.println("Client certificate information:");
            System.out.println("  Subject DN: " + x509Certificate.getSubjectDN());
            System.out.println("  Issuer DN: " + x509Certificate.getIssuerDN());
            System.out.println("  Serial number: " + x509Certificate.getSerialNumber());
            System.out.println("");
        }
    }

    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        for (X509Certificate x509Certificate : x509Certificates) {
            System.out.println("Server certificate information:");
            System.out.println("  Subject DN: " + x509Certificate.getSubjectDN());
            System.out.println("  Issuer DN: " + x509Certificate.getIssuerDN());
            System.out.println("  Serial number: " + x509Certificate.getSerialNumber());
            System.out.println("");
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
