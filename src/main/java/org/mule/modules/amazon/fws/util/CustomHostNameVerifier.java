package org.mule.modules.amazon.fws.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by IntelliJ IDEA.
 * User: davideason
 * Date: 6/10/11
 * Time: 3:32 PM
 */
public class CustomHostNameVerifier implements HostnameVerifier{
    public boolean verify(String s, SSLSession sslSession) {
        return false;
    }
}
