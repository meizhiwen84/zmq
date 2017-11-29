package com.jd.zmq.common.network.command;

/**
 * Created by meizhiwen on 2016/12/21.
 */
public class CommandFactory {

    public static Command create(Header header){
        if(header.getCommandType()==CommandType.PUT_MESSAGE){
            return new PutMessage();
        }else if(header.getCommandType()==CommandType.PUT_RESP_MESSAGE){
            return new PutRespMessage();
        }
        return null;
    }
}
