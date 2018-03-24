package me.ltang.smart.sso.rpc;

import java.io.Serializable;

/**
 * RPC回传用户对象(含菜单)
 *
 * @author Joe
 */
public class RpcUser implements Serializable {

    private static final long serialVersionUID = 4507869346123296527L;

    private Integer userId;
    // 登录名
    private String account;

    public RpcUser(String account) {
        super();
        this.account = account;
    }

    public RpcUser(Integer userId, String account) {
        super();
        this.userId = userId;
        this.account = account;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}