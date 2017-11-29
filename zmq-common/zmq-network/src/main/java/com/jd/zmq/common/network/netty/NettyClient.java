package com.jd.zmq.common.network.netty;

import com.jd.zmq.common.network.codec.NettyClientHandler;
import com.jd.zmq.common.network.codec.NettyDecoder;
import com.jd.zmq.common.network.codec.NettyEncoder;
import com.jd.zmq.common.network.command.CommandType;
import com.jd.zmq.common.network.command.Header;
import com.jd.zmq.common.network.command.HeaderType;
import com.jd.zmq.common.network.command.PutMessage;
import com.jd.zmq.common.network.session.ProducerId;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by meizhiwen on 2016/12/21.
 */
public class NettyClient {
    private EventLoopGroup ioGroup;

    private Bootstrap bootstrap;

    public void doStart() throws Exception {
        ioGroup=new NioEventLoopGroup(NettyConfig.workerThreads);
        bootstrap=new Bootstrap();
        bootstrap.group(ioGroup).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,false)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.SO_RCVBUF,1024*8)
                .option(ChannelOption.SO_SNDBUF,1024*8)
                .option(ChannelOption.SO_LINGER,-1)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new NettyEncoder())
                                .addLast(new NettyDecoder(65506,0,4))
                                .addLast(new NettyClientHandler());

//                                .addLast(new IdleStateHandler(0,0,10*1000, TimeUnit.MILLISECONDS));
                    }
                });

        InetSocketAddress address=new InetSocketAddress(NettyConfig.ip,NettyConfig.port);
        ChannelFuture channelFuture=bootstrap.connect(address).sync();
        if (!channelFuture.await(NettyConfig.timeout)) {
            System.out.println("============connect timeout");
        }
        final Channel channel=channelFuture.channel();

        final AtomicInteger i=new AtomicInteger(0);
        Timer timer=new Timer("client");
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {

                    for (int n=0;n<3;n++){
                        sendPutMessage(i,channel);
                        TimeUnit.SECONDS.sleep(60);
                    }

//            }
//        },3000,5000);
    }

    private void sendPutMessage(AtomicInteger i,Channel channel){
        PutMessage putMessage=new PutMessage();

        Header header=new Header();
        putMessage.setHeader(header);
        header.setHeaderType(HeaderType.REQUEST);
        header.setVersion((byte)1);
        header.setCommandType(CommandType.PUT_MESSAGE);
        header.setTime(System.currentTimeMillis());

        int num=i.getAndIncrement();
        header.setRequestId(Integer.valueOf("100257"+num));

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now=format.format(new Date());

        putMessage.setProducerId(new ProducerId("producerId :211257"+num));
        putMessage.setMessages("hi netty server:"+now+" i:"+(num)+"次");
        channel.writeAndFlush(putMessage).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println("==========send finished================");
            }
        });
    }
}
