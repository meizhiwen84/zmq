package com.jd.zmq.common.network.session;

/**
 * Created by meizhiwen on 2016/12/14.
 * 生产者id
 */
public class ProducerId {

//    private ConnectionId connectionId;

    public ProducerId(String producerId) {
        this.producerId = producerId;
    }

    private String producerId;

    public String getProducerId() {
        return producerId;
    }

    public void setProducerId(String producerId) {
        this.producerId = producerId;
    }
}
