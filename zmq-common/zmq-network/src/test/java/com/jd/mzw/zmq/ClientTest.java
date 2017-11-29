package com.jd.mzw.zmq;

import com.jd.zmq.common.network.netty.NettyClient;

/**
 * Created by meizhiwen on 2017/11/29.
 */
public class ClientTest {
    public static void main(String[] args) throws Exception {
        NettyClient nettyClient=new NettyClient();
        nettyClient.doStart();
    }
}
