
package com.trilink.ghbaqi.cainiaoshopping01.bean;


public class LoginRespMsg extends BaseRespMsg {


    private String token;

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
