package com.jd.zmq.common.network.codec;

import com.jd.zmq.common.network.command.PutRespMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by meizhiwen on 2016/12/21.
 */
public class NettyClientHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        PutRespMessage putRespMessage=(PutRespMessage) o;
        System.out.println(putRespMessage.getHeader());
        System.out.println("收到服务端发送过来的消息："+putRespMessage.getContent());
    }
}
