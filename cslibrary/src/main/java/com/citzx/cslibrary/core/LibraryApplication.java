package com.citzx.cslibrary.core;

import android.content.Context;

/**
 *Library上下文环境
 * 
 */
public class LibraryApplication {
    private static Context context;
    private static String ShouJiHao="";//13455262992
    private static String Token="";//0
    private static String UUID="";//0
    private static String AddressType="";
    public static String getAddressType() {
        return AddressType;
    }
    public static void setAddressType(String addressType) {
        AddressType = addressType;
    }
    public static void init(Context context) {
        LibraryApplication.context = context;
    }

    public static Context getContext() {
        return context;
    }

    public static String getShouJiHao() {
        return ShouJiHao;
    }

    public static void setShouJiHao(String shouJiHao) {
        ShouJiHao = shouJiHao;
    }

    public static String getToken() {
        return Token;
    }

    public static void setToken(String token) {
        Token = token;
    }

    public static String getUUID() {
        return UUID;
    }

    public static void setUUID(String UUID) {
        LibraryApplication.UUID = UUID;
    }
}
