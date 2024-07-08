package com.climawan.comp6844001.pertemuan5.hakubank.services;

import java.util.Base64;

/* loaded from: classes.dex */
public class AuthEncodingService {
    public static String encode(String input) {
        return rotate(Base64.getEncoder().encodeToString(input.getBytes()));
    }

    private static String rotate(String input) {
        int i;
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < input.length(); i2++) {
            char charAt = input.charAt(i2);
            if ((charAt < 'a' || charAt > 'm') && (charAt < 'A' || charAt > 'M')) {
                if ((charAt >= 'n' && charAt <= 'z') || (charAt >= 'N' && charAt <= 'Z')) {
                    i = charAt - '\r';
                }
                sb.append(charAt);
            } else {
                i = charAt + '\r';
            }
            charAt = (char) i;
            sb.append(charAt);
        }
        return sb.toString();
    }
}