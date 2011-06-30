package org.mule.modules.amazon.fws.util;

import org.mule.util.Base64;

import java.io.IOException;


/**
 * Created by IntelliJ IDEA.
 * User: davideason
 * Date: 6/1/11
 * Time: 7:35 PM
 *
 * This class defines common routines for encoding * data in AWS Platform re quests.
 *
 **/

public class Encoding {
/**
 * Performs base64-encoding of input bytes.
 * @param rawData Array of bytes to be encoded.
 * @return The base64 encoded string representation of rawData.
 * @throws java.io.IOException
 * */

    public static String EncodeBase64(byte[] rawData) throws IOException {
        return Base64.encodeBytes(rawData);
    }
}

