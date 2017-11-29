package com.jd.zmq.common.network.netty;

import com.jd.zmq.common.network.codec.NettyDecoder;
import com.jd.zmq.common.network.codec.NettyEncoder;
import com.jd.zmq.common.network.codec.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.channels.Pipe;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by meizhiwen on 2016/12/14.
 */
public class NettyServer {

    private EventLoopGroup bossGroup;

    private EventLoopGroup ioGroup;

    private ServerBootstrap serverBootstrap;

    /**
     * 服务器启动
     */
    public void doStart() throws Exception {
        bossGroup=new NioEventLoopGroup(NettyConfig.bossThreads,new DefaultThreadFactory("bossGroup"));
        ioGroup=new NioEventLoopGroup(NettyConfig.workerThreads,new DefaultThreadFactory("ioGroup"));

        InetSocketAddress inetSocketAddress=new InetSocketAddress(NettyConfig.ip,NettyConfig.port);

        serverBootstrap=new ServerBootstrap();
        serverBootstrap.group(bossGroup,ioGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,false)
                .option(ChannelOption.SO_BACKLOG,65535)
                .option(ChannelOption.SO_REUSEADDR,true)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.SO_RCVBUF,1024*8)
                .option(ChannelOption.SO_SNDBUF,1024*8)
                .option(ChannelOption.SO_LINGER,-1)
        .localAddress(inetSocketAddress)
        .childHandler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new NettyEncoder())
                        .addLast(new NettyDecoder(65506,0,4))
                        .addLast(new NettyServerHandler());
//                        .addLast(new IdleStateHandler(0,0,10*1000, TimeUnit.MILLISECONDS));
//                        .addLast(new ConnectionHander)
//                        .addLast(new UnmarshallerHandler());
            }
        })
        .bind().sync();
        System.out.println("=======nettyserver runed.........");
    }

}
