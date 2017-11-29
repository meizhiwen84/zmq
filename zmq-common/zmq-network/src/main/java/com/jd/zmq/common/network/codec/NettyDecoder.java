package com.jd.zmq.common.network.codec;

import com.jd.zmq.common.network.command.Command;
import com.jd.zmq.common.network.command.CommandFactory;
import com.jd.zmq.common.network.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.marshalling.CompatibleMarshallingDecoder;

/**
 * Created by meizhiwen on 2016/12/20.
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder{

    public NettyDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        //这个表示在调decode方法的时候，一会是一个完整的数据包的流，先根据偏移量，和长度本身的长度，获取到长度的值，然后再算出
        //后面要读取消息的长度，因此针对当前这个协议就应该是0,4,-4,0,因此长度字段是存的整个消息的长度。
        //重点理解这四个变量的含义 lengthFieldOffset lengthFieldLength lengthAdjustment initialBytesToStrip
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength,-4,0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf byteBuf=(ByteBuf) super.decode(ctx,in);
        if(byteBuf==null){
            return null;
        }

        Header header=new Header();
        header.decode(byteBuf);

        //根据头部类型创建不同的command
        Command command=CommandFactory.create(header);
        command.setHeader(header);
        command.decode(byteBuf);
        return command;
    }
}
