package org.kelompokwira.wirakopi.wirakopi.Secret;

public enum Secret {
    MID_CLIENT_KEY("SECRET_CLIENT"),
    MID_SERVER_KEY("SECRET_SERVER");
    private String secret = "";

    Secret(String theSecret) {
        this.secret = theSecret;
    }

    public String getSecret(){
        return secret;
    }
}
