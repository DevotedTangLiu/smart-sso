package me.ltang.smart.sso.client;

import java.io.Serializable;

/**
 * 已登录用户信息
 *
 * @author Joe
 */
public class SessionUser implements Serializable {

    private static final long serialVersionUID = 1764365572138947234L;

    /**
     * 登录用户访问Token
     */
    private String token;

    private Integer userId;

    /**
     * 登录名
     */
    private String account;

    public SessionUser() {
        super();
    }

    public SessionUser(String token, String account) {
        super();
        this.token = token;
        this.account = account;
    }

    public SessionUser(String token, Integer userId, String account) {
        super();
        this.userId = userId;
        this.token = token;
        this.account = account;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
