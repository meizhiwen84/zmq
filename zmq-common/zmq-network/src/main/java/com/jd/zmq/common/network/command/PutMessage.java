package com.jd.zmq.common.network.command;

import com.jd.zmq.common.network.codec.Serializer;
import com.jd.zmq.common.network.message.Message;
import com.jd.zmq.common.network.session.ProducerId;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * Created by meizhiwen on 2016/12/14.
 * 生产消息
 */
public class PutMessage extends Command {

    private ProducerId producerId;

    private String messages;

    public ProducerId getProducerId() {
        return producerId;
    }

    public void setProducerId(ProducerId producerId) {
        this.producerId = producerId;
    }

    @Override
    public void decode(ByteBuf byteBuf) throws UnsupportedEncodingException {
        String produerId=Serializer.readString(byteBuf);
        producerId=new ProducerId(produerId);
        messages=Serializer.readString(byteBuf);
    }

    @Override
    public void encodeBody(ByteBuf out) throws UnsupportedEncodingException {
        Serializer.writeString(producerId.getProducerId(),out);
        Serializer.writeString(messages,out);
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
