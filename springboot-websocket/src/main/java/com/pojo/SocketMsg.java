package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * @author Kevin
 * @date 2023/8/4 14:24
 */
@Data
public class SocketMsg implements Serializable {

    /**
     *  聊天类型 0：群聊，1：单聊
     */
    private Integer msgType;
    /**
     *  消息发送者
     */
    private String fromUser;
    /**
     *  消息接收者
     */
    private String toUser;
    /**
     *  消息
     */
    private String msg;
}
