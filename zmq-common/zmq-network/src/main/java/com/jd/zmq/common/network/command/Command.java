package com.jd.zmq.common.network.command;

import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * Created by meizhiwen on 2016/12/14.
 * 封装网络通信指令
 */
public class Command {

    private Header header;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void decode(ByteBuf byteBuf) throws UnsupportedEncodingException {
    }

    /**
     * 消息头部编码
     * @param out
     */
    public void encode(ByteBuf out) throws UnsupportedEncodingException {
        if(out==null){
            return;
        }
        int begin=out.writerIndex();//开始位置
        header.encode(out);
        this.encodeBody(out);
        int end=out.writerIndex();
        int size=end-begin;
        header.setLength(size);
        out.writerIndex(begin);
        out.writeInt(size);
        out.writerIndex(end);
    }

    public void encodeBody(ByteBuf out) throws UnsupportedEncodingException {

    }
}
