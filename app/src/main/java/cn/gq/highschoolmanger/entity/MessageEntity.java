package cn.gq.highschoolmanger.entity;

import android.os.Message;

public class MessageEntity {
    public String message_type ;
    public   Integer message_what ;
    public MessageEntity (String message_type,Integer message_what) {
        this.message_type =message_type ;
        this.message_what = message_what ;
    }
    public static Message getMessage (String message_type , Integer message_what) {
        Message msg = new Message() ;
        MessageEntity messageEntity = new MessageEntity(message_type, message_what);
        msg.obj = messageEntity;
        return msg ;
    }
}
