package com.trilink.ghbaqi.cainiaoshopping01.global;

import com.trilink.ghbaqi.cainiaoshopping01.bean.User;

/**
 * Created by ghbaqi on 2017/5/4.
 * 用户信息管理类
 */

public class UserManager {
    private static UserManager mInstance;
    private User mUser;
    private String mToken;

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public void clearUser(){
        mUser = null;
    }

    private UserManager() {
    }

    public static UserManager getInstace() {
        if (mInstance == null) {
            synchronized (UserManager.class) {
                if (mInstance == null)
                    mInstance = new UserManager();
            }
        }
        return mInstance;
    }

    public void clearToken() {
        mToken = null;
    }
}
