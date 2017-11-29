package com.jd.zmq.common.network.command;

import com.jd.zmq.common.network.codec.Serializer;
import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * Created by meizhiwen on 2016/12/21.
 */
public class PutRespMessage extends Command {

    private String content;

    @Override
    public void decode(ByteBuf byteBuf) throws UnsupportedEncodingException {
        content=Serializer.readString(byteBuf);
    }

    @Override
    public void encodeBody(ByteBuf out) throws UnsupportedEncodingException {
        Serializer.writeString(content,out);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
