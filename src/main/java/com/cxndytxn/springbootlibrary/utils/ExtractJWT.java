package com.cxndytxn.springbootlibrary.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJWT {

    public static String payloadJWTExtraction(String token, String extraction) {

        //Replace keyword "Bearer" with empty string
        token.replace("Bearer ", "");

        //Split JWT token into three chunks -> header, payload, and signature
        String[] chunks = token.split("\\.");

        //Prepare a decoder because JWT is encoded
        Base64.Decoder decoder = Base64.getUrlDecoder();

        //Decode the chunk of payload only
        String payload = new String(decoder.decode(chunks[1]));

        //Split payload by comma
        String[] entries = payload.split(",");

        Map<String, String> map = new HashMap<>();

        //Loop through entries to find the one with key "sub"
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            if (keyValue[0].equals(extraction)) {
                int remove = 1;
                if (keyValue[1].endsWith("}")) {
                    remove = 2;
                }
                keyValue[1] = keyValue[1].substring(0, keyValue[1].length() - remove);
                keyValue[1] = keyValue[1].substring(1);

                map.put(keyValue[0], keyValue[1]);
            }
        }

        if (map.containsKey(extraction)) {
            return map.get(extraction);
        }
        return null;
    }
}
