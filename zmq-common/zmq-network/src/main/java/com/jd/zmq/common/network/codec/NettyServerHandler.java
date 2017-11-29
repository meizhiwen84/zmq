package com.jd.zmq.common.network.codec;

import com.jd.zmq.common.network.command.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.Date;

/**
 * Created by meizhiwen on 2016/12/21.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        PutMessage command=(PutMessage)o;
        System.out.println(command.getHeader());
        System.out.println("收到客户端发送过来的消息："+"producerId:"+command.getProducerId().getProducerId()+" message:"+command.getMessages());

        PutRespMessage putRespMessage=new PutRespMessage();

        Thread.sleep(2000l);
        String respTime= DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
        long now=System.currentTimeMillis();
        putRespMessage.setContent("我收到你的消息，给你返回："+now+" 响应时间为："+respTime);

        Header header=new Header();
        header.setTime(System.currentTimeMillis());
        header.setRequestId(12321);
        header.setCommandType(CommandType.PUT_RESP_MESSAGE);
        header.setHeaderType(HeaderType.RESPONSE);
        header.setVersion((byte)1);
        header.setStatus(200);
        header.setErrorMsg("接收成功");
        putRespMessage.setHeader(header);

        ctx.writeAndFlush(putRespMessage);
    }
}
