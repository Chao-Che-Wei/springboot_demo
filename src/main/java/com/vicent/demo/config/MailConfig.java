package com.vicent.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "mail")
@PropertySource("classpath:mail.properties")
public class MailConfig {

    private String host;
    private int port;
    private boolean authEnabled;
    private boolean starttlsEnabled;
    private String userAddress;
    private String userPwd;
    private String userDisplayName;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean isAuthEnabled() {
        return authEnabled;
    }

    public boolean isStarttlsEnabled() {
        return starttlsEnabled;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAuthEnabled(boolean authEnabled) {
        this.authEnabled = authEnabled;
    }

    public void setStarttlsEnabled(boolean starttlsEnabled) {
        this.starttlsEnabled = starttlsEnabled;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }
}
