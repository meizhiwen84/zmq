package com.jd.zmq.common.network.session;

/**
 * Created by meizhiwen on 2016/12/14.
 * 客户端ID
 */
public class ClientId {
    //客户端版本
    private String version;
    //16进制ip
    private String ip;
    //客户端id
    private String clientId;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
