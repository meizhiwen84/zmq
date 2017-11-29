package com.jd.zmq.common.network.codec;

import com.jd.zmq.common.network.command.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by meizhiwen on 2016/12/21.
 */
public class NettyEncoder extends MessageToByteEncoder<Command> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Command command, ByteBuf byteBuf) throws Exception {
        command.encode(byteBuf);
    }
}
