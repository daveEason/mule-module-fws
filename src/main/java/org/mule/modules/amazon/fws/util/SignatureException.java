package org.mule.modules.amazon.fws.util;

/**
 * Created by IntelliJ IDEA.
 * User: davideason
 * Date: 7/2/11
 * Time: 2:39 PM
 * Signature exception used to report issues generating AWS signatures.
 */
public class SignatureException extends RuntimeException {

    public SignatureException(String msg){
        super(msg);
    }

    public SignatureException(String msg, Throwable cause){
        super(msg,cause);
    }

}
