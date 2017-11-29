package com.jd.zmq.common.network.codec;

import com.jd.zmq.common.Constans;
import com.jd.zmq.common.network.command.Command;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * Created by meizhiwen on 2016/12/21.
 */
public class Serializer {

    /**
     * 读取一个字符串
     * 协议是：一个int型表示要读取的字节长度，然后再读取这么长的字节数组
     * @param byteBuf
     * @return
     */
    public static String readString(ByteBuf byteBuf) throws UnsupportedEncodingException {
        if(byteBuf==null){
            throw new IllegalArgumentException("byteBuf is null");
        }
        int length=byteBuf.readInt();
        if(byteBuf.readableBytes()>=length){
            byte[] bytes=new byte[length];
            byteBuf.readBytes(bytes);
            return new String(bytes, Constans.encodeString);
        }
        return new String(new byte[0],Constans.encodeString);
    }

    public static void writeString(String value,ByteBuf byteBuf) throws UnsupportedEncodingException {
        if(byteBuf==null){
            throw new IllegalArgumentException("byteBuf is null");
        }
        byte[] bytes=value.getBytes(Constans.encodeString);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
