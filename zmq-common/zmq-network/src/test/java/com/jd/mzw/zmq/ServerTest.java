package com.jd.mzw.zmq;

import com.jd.zmq.common.network.netty.NettyServer;

/**
 * Created by meizhiwen on 2017/11/29.
 */
public class ServerTest {
    public static void main(String[] args) throws Exception {
        NettyServer nettyServer=new NettyServer();
        nettyServer.doStart();
    }
}
