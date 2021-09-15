package com.flavorplus.login.repository;

public class LoginResponse {
    private int RES_CODE;
    private String RES_MSG;
    private String AUTH;
    private int USER_ID;
    private int ACCOUNT_TYPE;
    private String DISPLAY_NAME;
    private String EMAIL;

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public int getRES_CODE() {
        return RES_CODE;
    }

    public void setRES_CODE(int RES_CODE) {
        this.RES_CODE = RES_CODE;
    }

    public String getRES_MSG() {
        return RES_MSG;
    }

    public void setRES_MSG(String RES_MSG) {
        this.RES_MSG = RES_MSG;
    }

    public String getAUTH() {
        return AUTH;
    }

    public void setAUTH(String AUTH) {
        this.AUTH = AUTH;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public int getACCOUNT_TYPE() {
        return ACCOUNT_TYPE;
    }

    public void setACCOUNT_TYPE(int ACCOUNT_TYPE) {
        this.ACCOUNT_TYPE = ACCOUNT_TYPE;
    }

    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }
}
