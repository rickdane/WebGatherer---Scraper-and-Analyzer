package com.rickdane.springmodularizedproject.api.transport;

/**
 * @author Rick Dane
 */
public class TransportBase {

    protected String userName;

    protected String passwordEncrytped;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordEncrytped() {
        return passwordEncrytped;
    }

    public void setPasswordEncrytped(String passwordEncrytped) {
        this.passwordEncrytped = passwordEncrytped;
    }
}
