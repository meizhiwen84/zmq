package com.jd.zmq.common.network.command;

import com.jd.zmq.common.network.codec.Serializer;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * Created by meizhiwen on 2016/12/14.
 * 封装消息头
 */
public class Header {

    private HeaderType headerType;//0表示请求 1表示应答
    //协议版本号
    private byte version=1;
    //命令类型
    private int commandType;
    //请求ID
    private int requestId;
    //整个消息的长度
    private int length;
    //请求或者响应的时间
    private long time;
    //状态码
    private int status;
    //响应错误信息
    private String errorMsg;

    /**
     * 消息头部编码
     * @param out
     */
    public void encode(ByteBuf out) throws UnsupportedEncodingException {
        out.writeInt(length);//消息的总长度先占位
        out.writeByte(version);
        out.writeInt(commandType);
        out.writeInt(requestId);
        out.writeLong(time);
        if(headerType.equals(HeaderType.REQUEST)){
            out.writeByte(0);
        }else{
            out.writeByte(1);
            out.writeInt(status);
            Serializer.writeString(errorMsg,out);
        }


    }

    /**
     * 消息头部解码
     * @param in
     * @throws UnsupportedEncodingException
     */
    public void decode(ByteBuf in) throws UnsupportedEncodingException {
        length=in.readInt();//先读取总长度
        version=in.readByte();//
        commandType=in.readInt();//
        requestId=in.readInt();
        time=in.readLong();
        byte ht=in.readByte();
        if(ht==0){
            headerType=HeaderType.REQUEST;
        }else{
            headerType=HeaderType.RESPONSE;
            status=in.readInt();//一个int
            //先读取错误信息的字节长度，占2个字节
            errorMsg=Serializer.readString(in);
        }
    }

    public HeaderType getHeaderType() {
        return headerType;
    }

    public void setHeaderType(HeaderType headerType) {
        this.headerType = headerType;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "Header{" +
                "headerType=" + headerType +
                ", version=" + version +
                ", commandType=" + commandType +
                ", requestId=" + requestId +
                ", length=" + length +
                ", time=" + time +
                ", status=" + status +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
